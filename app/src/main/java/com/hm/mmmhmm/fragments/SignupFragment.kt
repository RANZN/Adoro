package com.hm.mmmhmm.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity
import com.hm.mmmhmm.helper.CommanFunction
import com.hm.mmmhmm.helper.ConnectivityObserver
import com.hm.mmmhmm.helper.SessionManager
import com.hm.mmmhmm.helper.toast
import com.hm.mmmhmm.models.RequestAuthenticateUsername
import com.hm.mmmhmm.models.RequestRegister
import com.hm.mmmhmm.web_service.ApiClient
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.HashMap


class SignupFragment : Fragment() {

    var isUsernameAvailable=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    private fun setupToolBar() {
//        ivBack?.setBackgroundResource(R.drawable.ic_back_arrow)
//        tb_root?.setBackgroundColor(resources.getColor(R.color.black))
        tv_toolbar_title?.setText("Adoro")
        tv_toolbar_title?.setTextColor(resources.getColor(R.color.black))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolBar()
        //GlobleData.goToLoginScreen = false

        btn_signup.setOnClickListener(View.OnClickListener {
            validateInput()
        })
        et_username.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateUsername(s.toString())
            }
        })


    }

//    private fun stopFragment() {
//        startActivity(Intent(activity, MainActivity::class.java))
//        activity?.finish()
//    }


    private fun validateInput() {
        val name = et_name.text.toString()
        val email = et_email.text.toString()
        val number = requireArguments().getString("number")
        val username = et_username.text.toString()
        if (name.isNullOrEmpty()) {
            toast(R.string.full_name, 1)
        } else if (email.isNullOrEmpty()) {
            toast(R.string.email_address, 1)
        } else if (username.isNullOrEmpty()) {
            toast(R.string.username, 1)
        } else if (!isUsernameAvailable) {
            toast(R.string.username_already_taken, 1)
        } else if (ConnectivityObserver.isOnline(activity as Context)) {
            var requestRegister: RequestRegister = RequestRegister(name,number?.toLong(),email,username,SessionManager.getFCMToken()?:"");
            hitRegisteruserAPI(requestRegister)

        }
    }
    private fun validateUsername(username: String) {
    if (username.isNullOrEmpty()) {
            toast(R.string.username, 1)
        et_username.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    } else if (ConnectivityObserver.isOnline(activity as Context)) {
            var requestAuthenticateUsername: RequestAuthenticateUsername = RequestAuthenticateUsername(username);
        hitAuthenticateUsernameAPI(requestAuthenticateUsername)
        }
    }

    private fun showDialog() {
        val dialog = Dialog(activity as Context)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window?.setBackgroundDrawable( ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.setCancelable(false)
        val body = dialog.findViewById(R.id.tvBody) as TextView
        //body.text = title
        val title = dialog.findViewById(R.id.tvTitle) as TextView
        // body.text = title
        val yesBtn = dialog.findViewById(R.id.btn_ok) as Button
        yesBtn.setOnClickListener {
            //todo
            openNext()
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun openNext() {
        startActivity(Intent(requireContext(), MainActivity::class.java))
        activity?.finishAffinity()
    }

    private fun hitRegisteruserAPI( requestRegister: RequestRegister) {
        pb_signup.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.registerUser(requestRegister)
                withContext(Dispatchers.Main) {

                    try {
                        pb_signup.visibility = View.GONE
                        if (response.body()?.OK != null) {
//                        Toast.makeText(activity," "+response.body()?.message, Toast.LENGTH_SHORT).show()
                            val r = response.body()
                            SessionManager.init(activity as Context)
                            SessionManager.setLoginStatus("true")
                            SessionManager.setUserId(response.body()?.OK!!._id)

                            SessionManager.setRefrerCode(
                                response.body()?.OK?.referCode ?: ""
                            )

                            showDialog()

                            loginUserForFirebase()

                        } else {
                            CommanFunction.handleApiError(
                                response.errorBody()?.charStream(),
                                requireContext()
                            )
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun pushUserToFirebase() {
        val reference = FirebaseDatabase.getInstance().getReference("users")
        reference.get().addOnSuccessListener {
            try {
                val value = it.value as HashMap<String?, Any>
                value.apply {
                    put(SessionManager.getFirebaseID(), HashMap<String, Any?>().apply {
                        put("userId", SessionManager.getFirebaseID())
                        put("userName", SessionManager.getUserName())
                        put("email", SessionManager.getUserEmail())
                        put("isOnline", true)
                    })
                }
                reference.updateChildren(value)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loginUserForFirebase() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            SessionManager.getUserEmail(),
            "test@123"
        ).addOnSuccessListener {
            SessionManager.setFirebaseID(it.user?.uid)
            FirebaseAuth.getInstance().signOut()
            pushUserToFirebase()
        }.addOnFailureListener {
            FirebaseAuth.getInstance().signOut()
            if (it is FirebaseAuthUserCollisionException) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    SessionManager.getUserEmail(),
                    "test@123"
                ).addOnSuccessListener { result ->
                    SessionManager.setFirebaseID(result.user?.uid)
                    FirebaseAuth.getInstance().signOut()
                    pushUserToFirebase()
                }
            }
        }
    }

    private fun hitAuthenticateUsernameAPI( requestAuthenticateUsername: RequestAuthenticateUsername) {
        pb_signup.visibility = View.VISIBLE
        val apiInterface = ApiClient.getRetrofitService(requireContext())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiInterface.authenticateUsername(requestAuthenticateUsername)
                withContext(Dispatchers.Main) {

                    try {
                        pb_signup.visibility = View.GONE
                        if (response.body()?.OK != null) {
//                        Toast.makeText(activity," "+response.body()?.message, Toast.LENGTH_SHORT).show()
                            val r = response.body()
                            if(r?.OK?.length!=0){
                                et_username.error =  resources.getString(R.string.username_already_taken)
                                isUsernameAvailable= false
                            }else{
                                et_username.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.tick, 0);
                                isUsernameAvailable= true
                            }
                        } else {
                            CommanFunction.handleApiError(
                                response.errorBody()?.charStream(),
                                requireContext()
                            )
                        }
                    } catch (e: Exception) {
                        Toast.makeText(requireActivity(), "" + e.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {

            }
        }
    }
}