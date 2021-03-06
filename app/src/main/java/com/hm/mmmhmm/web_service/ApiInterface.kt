package com.hm.mmmhmm.web_service

import com.hm.mmmhmm.helper.Constants
import com.hm.mmmhmm.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST("api/sign-me-up")
    suspend fun registerAPI(
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("password") password: String,
        @Field("password_confirmation") password_confirmation: String,
        @Field("phone") phone: String,
        @Field("gender") gender: String,
        @Field("referal_code") referal_code: String,

        @Field("device_id") device_id: String,
        @Field("device_type") device_type: String
    ): retrofit2.Response<RegisterModel>

    @GET("api/user-details")
    suspend fun getUserDetailApi(): retrofit2.Response<UserDetailModel>



    @FormUrlEncoded
    @POST("api/update-profile")
    suspend fun updateUserInfoApi(
        @Field("full_name") full_name: String,
        @Field("user_address") user_address: String,
        @Field("user_phone") user_phone: String,
        @Field("user_gender") user_gender: String,
        @Field("user_dob") user_dob: String,
        @Field("user_identity_number") user_identity_number: String,
        @Field("user_sexual_orientation") user_sexual_orientation: String
    ): retrofit2.Response<UpdateUserInfoModel>

    @Multipart
    @POST("api/update-profile-pic")
    suspend fun updateProfilePicApi(@Part image: MultipartBody.Part): retrofit2.Response<UpdateProfilePicModel>


//    -----------------2021---------------------


    @GET("_functions/getCampaignList")
    suspend fun getCampaignList(): retrofit2.Response<BaseResponse>


    @GET("_functions/getResults")
    suspend fun getResults(): retrofit2.Response<BaseResponse>

    @GET("_functions/getContest")
    suspend fun getContest(): retrofit2.Response<GetContestModel>

    @POST("_functions/getSpecificResultData")
    suspend fun getSpecificResultData(
        @Body() req: GeneralRequest
    ): retrofit2.Response<BaseResponse>


    @POST("_functions/updateDescription")
    suspend fun updateDescription(
        @Body() req: UpdateGroupRequest
    ): retrofit2.Response<BaseResponse>


    @POST("_functions/deletePost")
    suspend fun deletePost(
        @Body() req: UpdateGroupRequest
    ): retrofit2.Response<BaseResponse>


    @POST("_functions/groupPost")
    suspend fun groupPost(
        @Body() req: PostGroupRequest
    ): retrofit2.Response<BaseResponse>


    @GET("_functions/showPost")
    suspend fun getFeed(
    ): retrofit2.Response<CommentsData>

    @POST("_functions/getSpecificPostDetail")
    suspend fun getSpecificPostDetail(
        @Body() req: GeneralRequest
    ): retrofit2.Response<CommentsData>


    @POST("_functions/postTemplate")
    suspend fun publishTemplatePostAPI(
        @Body() req: PublishMemePostTempelate
    ): retrofit2.Response<ResponseBody>


    @POST("_functions/getSpecificPostDetailGroup")
    suspend fun getSpecificPostDetailGroup(
        @Body() req: GeneralRequest
    ): retrofit2.Response<BaseResponse>

    @POST("_functions/getSpecificDiscussionDetailGroup")
    suspend fun getSpecificDiscussionDetailGroup(
        @Body() req: GeneralRequest
    ): retrofit2.Response<BaseResponse>

    @POST("_functions/showNotification")
    suspend fun showNotification(
        @Body() req: GeneralRequest
    ): retrofit2.Response<BaseResponse>

    @GET()
    suspend fun getIp(@Url url:String="https://api.ipify.org/?format=json"):GetIpAddressClass

    @POST("_functions/checkIPAddress")
    suspend fun checkIp(
        @Body() req: IPAddressCheck
    ): IpStatusModel

    @POST("_functions/updateUserNotificationStatus")
    suspend fun updateUserNotificationStatus(
        @Body() req: UpdateUserNotificationStatusRequest
    ): retrofit2.Response<BaseResponse>

    @GET("_functions/getWalkThroughList")
    suspend fun getWalkThroughList(): retrofit2.Response<BaseResponse>

    @GET("_functions/browseGroup")
    suspend fun browseGroup(): retrofit2.Response<BaseResponse>

    @GET("_functions/getCategoryListForGroup")
    suspend fun getCategoryListForGroup(): retrofit2.Response<BaseResponse>



    @POST("_functions/getSpecificCampaignData")
    suspend fun getCampaignDetail(
        @Body() req: RequestCampaign
    ): retrofit2.Response<CampaignDetailModel>


    @POST("_functions/sendSMS")
    suspend fun sendOTP(
        @Body() req: OTPRequest
    ): retrofit2.Response<BaseResponse>



    @POST("_functions/updateProfile")
    suspend fun updateProfile(
        @Body() req: UpdateProfileRequest
    ): retrofit2.Response<BaseResponse>

    @POST("_functions/authenticate")
    suspend fun registerNumber(
        @Body() req: RequestAuthenticateNumber
    ): retrofit2.Response<BaseResponse>

    @POST("_functions/createGroup")
    suspend fun createGroup(
        @Body() req: CreateGroupRequest
    ): retrofit2.Response<BaseResponse>

    @POST("_functions/updateGroup")
    suspend fun updateGroup(
        @Body() req: EditGroupRequest
    ): retrofit2.Response<BaseResponse>

    @POST("_functions/signup")
    suspend fun registerUser(
        @Body() req: RequestRegister
    ): retrofit2.Response<RegisterModel>

    @POST("_functions/login")
    suspend fun loginUser(
        @Body() req: RequestLogin
    ): retrofit2.Response<LoginResponse>


    @POST("_functions/referalSignup")
    suspend fun registerReferalUser(
        @Body() req: ReferRequestRegister
    ): retrofit2.Response<RegisterModel>

    @POST("_functions/authenticateUsername")
    suspend fun authenticateUsername(
        @Body() req: RequestAuthenticateUsername
    ): retrofit2.Response<BaseResponse>

    @POST("_functions/showAdoro")
    suspend fun showAdoro(
        @Body() req: GeneralRequest
    ): retrofit2.Response<BaseResponse>

    @POST("_functions/deletePost")
    suspend fun deletePost(
        @Body() req: GeneralRequest
    ): retrofit2.Response<BaseResponse>


    @POST("_functions/getProfilePost")
    suspend fun getProfilePost(
        @Body() req: GeneralRequest
    ): retrofit2.Response<BaseResponse>

    @POST("_functions/showTrancations")
    suspend fun showTrancations(
        @Body() req: GeneralRequest
    ): retrofit2.Response<BaseResponse>


    @POST("_functions/addMemberToGroup")
    suspend fun addMemberToGroup(
        @Body() req: JoinGroupRequest
    ): retrofit2.Response<ResponseBody>

    @POST("_functions/showAnnouncement")
    suspend fun showAnnouncement(
        @Body() req: ShowAnnouncementRequest
    ): retrofit2.Response<BaseResponse>


    @POST("_functions/getSpecificGroupData")
    suspend fun getSpecificGroupData(
        @Body() req: GetSpecificGroupDataRequest
    ): retrofit2.Response<BaseResponse>

    @POST("_functions/showGroupPost")
    suspend fun showGroupPost(
        @Body() req: ShowAnnouncementRequest
    ): retrofit2.Response<BaseResponse>

    @POST("_functions/showGroupDiscussion")
    suspend fun showGroupDiscussion(
        @Body() req: ShowAnnouncementRequest
    ): retrofit2.Response<BaseResponse>

    @POST("_functions/groupDiscussionPostUpdateLike")
    suspend fun groupDiscussionPostUpdateLike(
        @Body() req: Item
    ): retrofit2.Response<BaseResponse>

    @POST("_functions/campaignUpdateLike")
    suspend fun campaignUpdateLike(
        @Body() req: CampaignUpdateLikeRequest
    ): retrofit2.Response<BaseResponse>

    @POST("_functions/groupMemePostUpdateLike")
    suspend fun groupMemePostUpdateLike(
        @Body() req: Item
    ): retrofit2.Response<BaseResponse>

    @POST("_functions/reportCampaign")
    suspend fun reportCampaign(
        @Body() req: GeneralRequest
    ): retrofit2.Response<BaseResponse>


    @POST("_functions/updateLike")
    suspend fun updateLike(
        @Body() req: ItemComment
    ): retrofit2.Response<BaseResponse>

    @POST("_functions/searchAccount")
    suspend fun searchAccount(
        @Body() req: SearchRequest
    ): retrofit2.Response<BaseResponse>

    @POST("_functions/searchCampaign")
    suspend fun searchCampaign(
        @Body() req: SearchRequest
    ): retrofit2.Response<BaseResponse>

    @POST("_functions/searchGroup")
    suspend fun searchGroup(
        @Body() req: SearchRequest
    ): retrofit2.Response<BaseResponse>


    @POST("_functions/getUserData")
    suspend fun getUserData(
        @Body() req: ProfileRequest
    ): retrofit2.Response<BaseResponse>

    @POST("_functions/follow")
    suspend fun follow(
        @Body() req: FollowRequest
    ): retrofit2.Response<BaseResponse>


    @POST("_functions/getSessionID")
    suspend fun getSessionID(
        @Body() req: GeneralRequest
    ): retrofit2.Response<BaseResponse>


    @POST("_functions/sendWithdrawalRequest")
    suspend fun sendWithdrawalRequest(
        @Body() req: RequestWithdrawalMoney
    ): retrofit2.Response<BaseResponse>


    @POST("_functions/shiftRequestToExisting")
    suspend fun acceptMemberJoinGroupRequest(
        @Body() req: AcceptGroupRequest
    ): retrofit2.Response<BaseResponse>



    @POST("_functions/deleteMemberData")
    suspend fun deleteMemberData(
        @Body() req: DeleteMemberRequest
    ): retrofit2.Response<BaseResponse>


    @POST("_functions/deleteRequestedMemberData")
    suspend fun deleteRequestedMemberData(
        @Body() req: DeleteMemberRequest
    ): retrofit2.Response<BaseResponse>


    @POST("_functions/showMyTemplate")
    suspend fun showMyTemplate(
        @Body() req: RequestShowMyTemplate
    ): retrofit2.Response<BaseResponse>

    @POST("_functions/postTemplate")
    suspend fun postTemplate(
        @Body() req: PostTemplateRequest
    ): retrofit2.Response<BaseResponse>


    @POST("_functions/updateComment")
    suspend fun updateComment(
        @Body() req: PostCommentRequest
    ): retrofit2.Response<BaseResponse>

    @POST("_functions/grouDiscussionPostUpdateComment")
    suspend fun grouDiscussionPostUpdateComment(
        @Body() req: PostCommentRequest
    ): retrofit2.Response<BaseResponse>

    @POST("_functions/addAdoro")
    suspend fun addAdoro(
        @Body() req: AddAdoroCoinsRequest
    ): retrofit2.Response<ResponseBody>

    @GET("_functions/browseTemplate")
    suspend fun browseTemplate(): retrofit2.Response<BaseResponse>

    @FormUrlEncoded
    @POST("api/update-fbtoken")
    suspend fun updateFcmToken(
        @Field("device_type") device_type: String,
        @Field("device_id") device_id: String
    ): retrofit2.Response<DefaultResponse>


    @POST("_functions/publishPost")
    suspend fun publishPostAPI(
        @Body() req: PublishPostRequest
    ): retrofit2.Response<BaseResponse>



    @POST("_functions/submitMemeToCampaign")
    suspend fun submitMemeToCampaign(
        @Body() req: RequestPublishCampaign
    ): retrofit2.Response<BaseResponse>


//    @Multipart
//    @POST("_functions/publishPost")
//    suspend fun publishPostAPI(
//        @Part image: MultipartBody.Part,
//        @Body() requestBody: RequestBody
//    ): retrofit2.Response<BaseResponse>
//


    @Multipart
    @POST("api/reply-invite")
    suspend fun updateInvite(
        @Part("invite_id") id: RequestBody,
        @Part("agmt_sex_check") agmt_sex_check: RequestBody,
        @Part("agmt_oral_check") agmt_oral_check: RequestBody,
        @Part("agmt_fondling_check") agmt_fondling_check: RequestBody,
        @Part("agmt_other_conduct_custom") agmt_other_conduct_custom: RequestBody,
        @Part image_url: MultipartBody.Part?
    ): retrofit2.Response<BaseResponse>


    @POST
    suspend fun subscribeNotifications(
        @Url url: String = "https://fcm.googleapis.com/fcm/send",
        @Header("Authorization") key: String = Constants.firebaseKey,
        @Header("Content-Type") contextType: String = "application/json",
        @Body notification: NotificationPublish
    )
}