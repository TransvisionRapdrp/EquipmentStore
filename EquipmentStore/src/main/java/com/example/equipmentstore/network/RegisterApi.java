package com.example.equipmentstore.network;



import com.example.equipmentstore.models.AttendanceSummary;
import com.example.equipmentstore.models.Complaints;
import com.example.equipmentstore.models.EquipmentDetails;
import com.example.equipmentstore.models.EquipmentModel;
import com.example.equipmentstore.models.LoginDetails;
import com.example.equipmentstore.models.PostOfficeModel;
import com.example.equipmentstore.models.Staff_OutGoingModel;
import com.example.equipmentstore.models.ViewComplaint;
import com.example.equipmentstore.models.VisitorModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RegisterApi {

    @POST("Retrofit_loginDetails")
    Call<List<LoginDetails>> getlogin(@Body LoginDetails loginModel);

    @POST("Equipment_Insert")
    Call<EquipmentModel> getequipmentInsert(@Body EquipmentModel equipmentModel);

    @POST("View_received_approval")
    @FormUrlEncoded
    Call<List<EquipmentDetails>> getReceivedApproval(@Field("Subdivcode") String Subdivcode);

    @POST("Received_Approval")
    Call<EquipmentDetails> receivedApproval(@Body EquipmentDetails equipmentDetails);

    @GET("HardwareComplaints")
    Call<List<ViewComplaint>> getComplaints();

    @POST("Mgmt_Approval")
    @FormUrlEncoded
    Call<EquipmentModel> managementApproval(@Field("ID")String ID);

    @GET("Equipment_Details")
    Call<List<EquipmentModel>> getData();

    @GET("Equipment_Names")
    Call<List<EquipmentModel>> getEquipmentName();

   /* @POST("Equipment_Insert")
    @FormUrlEncoded
    Call<List<EquipmentModel>> getequipmentInsert(@Field("USER_NAME") String USER_NAME, @Field("MOBILE_NO") String MOBILE_NO, @Field("SUBDIV_CODE") String SUBDIV_CODE,
                                               @Field("ITEMS") String ITEMS, @Field("REMARK") String REMARK, @Field("STOCK_ROLLS") String STOCK_ROLLS);
*/


//    @POST("Visitor")
//    @FormUrlEncoded
//    Call<List<EquipmentDetails>> getvisitorinfo(@Field("Dateofvisit") String Dateofvisit, @Field("VisitorsName") String VisitorsName, @Field("FromVisitor") String FromVisitor,
//                                                  @Field("PurposeofVisit") String PurposeofVisit, @Field("ToMeet") String ToMeet, @Field("ContactNumber") String ContactNumber, @Field("PriorityAppointed") String PriorityAppointed, @Field("Mailid") String Mailid,@Field("InTime") String InTime,@Field("Outtime") String Outtime,@Field("Remarks") String Remarks,@Field("Incharge") String Incharge);


    @POST("Visitor")
    Call<EquipmentDetails> getvisitorinfo(@Body VisitorModel visitorModel);
   /* @POST("PostofficeLetter")
    @FormUrlEncoded
    Call<List<PostOfficeModel>> postLetter(@Field("Type_of_post") String Type_of_post, @Field("InOut") String InOut, @Field("LetterFrom") String LetterFrom, @Field("LetterTo") String LetterTo, @Field("DocketNo") String DocketNo, @Field("PostalName") String PostalName, @Field("ReceivedName") String ReceivedName, @Field("Dispatch")
            String Dispatch, @Field("Authorized") String Authorized, @Field("Senders") String Senders, @Field("Date") String Date);*/

    @POST("PostofficeLetter")
    Call<PostOfficeModel> insertPostLetter(@Body PostOfficeModel postOfficeModel);

   /* @POST("Staff_Outgoing")
    @FormUrlEncoded
    Call<List<Staff_OutGoingModel>> staffOutGoing(@Field("Date") String Date, @Field("Name") String Name, @Field("Visit_To") String Visit_To, @Field("Reason") String Reason, @Field("Mode_Of_Transport") String Mode_Of_Transport, @Field("Out_time") String Out_time, @Field("IN__time") String IN__time);*/


    @POST("Staff_Outing")
    Call<Staff_OutGoingModel> insertStaffOutgoing(@Body Staff_OutGoingModel staff_outGoingModel);

    @POST("MPIN_Resister")
    Call<EquipmentDetails> getmpin_regNum(@Body LoginDetails loginDetails);

    @POST("MPIN_Login")
    Call<List<LoginDetails>> getmpinlogin(@Body LoginDetails loginDetails);

    @POST("Spot_Attendance_Details")//8
    @FormUrlEncoded
    Call<List<AttendanceSummary>> attendanceInsert(@Field("EMINO") String EMINO, @Field("EMPID") String EMPID, @Field("EMPNAME") String EMPNAME, @Field("PHOTO") String PHOTO,
                                                   @Field("LOG") String LOG, @Field("LAT") String LAT, @Field("REMARK") String REMARK,
                                                   @Field("ADDRESS") String ADDRESS, @Field("Encodefile") String Encodefile);

    @POST("Branchname")
    Call<List<PostOfficeModel>> getAddresses();

    @POST("HW_Complaint_Insert")
    Call<Complaints> insertComplaints(@Body Complaints complaints);

    @GET("HardwareComplaints")
    Call<List<Complaints>> getHardwareComplaints();

    @POST("View_PostLetter_Receive")
    @FormUrlEncoded
    Call<List<PostOfficeModel>> getPostDetails(@Field("Letter_to")String Letter_to);

    @POST("Post_Letter_Received")
    Call<EquipmentDetails> insertReceviedPost(@Body  EquipmentDetails postOfficeModel);

    @POST("Monthly_AttendanceSummary")
    @FormUrlEncoded
    Call<List<AttendanceSummary>> getempsummary(@Field("Month_Year") String Month_Year,@Field("User_ID") String User_ID);

    @POST("SUBDIV_SIGN")
    @FormUrlEncoded
    Call<PostOfficeModel> subDivisionApproval(@Field("ID") String ID);

    @POST("DIV_SIGN")
    @FormUrlEncoded
    Call<PostOfficeModel> divisionApproval(@Field("ID") String ID);

    @POST("Post_Letter_Status_Log")
    Call<PostOfficeModel> postOfficeRemark(@Body PostOfficeModel postOfficeModel);
}
