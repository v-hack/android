package ru.nordclan.myapplication.api

import com.fasterxml.jackson.annotation.JsonProperty
import org.joda.time.LocalDateTime

sealed class ApiResponse

data class ApiError(val message: String, val code: Int) : ApiResponse()

data class NetworkError(val message: String, val code: Int) : ApiResponse()

data class LoginResponse(val token: String) : ApiResponse()

object OkResponse : ApiResponse()

data class VisitResponse(
    val date: LocalDateTime,
    @JsonProperty("patient_id")
    val patientId: Long,
    @JsonProperty("doctor_id")
    val doctorId: Long,
    @JsonProperty("doctor_full_name")
    val doctorFullName: String,
    val specialization: String
) : ApiResponse()

data class DrugResponse(
    @JsonProperty("DrugId")
    val drugId: Long,
    @JsonProperty("PatientId")
    val patientId: Long,
    @JsonProperty("Name")
    val name: String,
    @JsonProperty("DrugType")
    val drugType: String,
    @JsonProperty("FrequencyAdmission")
    val frequencyAdmission: String,
    @JsonProperty("FeaturesReception")
    val featuresReception: String,
    @JsonProperty("StartReception")
    val startReception: LocalDateTime?,
    @JsonProperty("FinishReception")
    val finishReception: LocalDateTime?,
//    @JsonProperty("Times")
//    val times: List<String>?,
    @JsonProperty("Allowed")
    val allowed: Boolean
) : ApiResponse()

data class Research(
    @JsonProperty("research_id")
    val researchId: Long,
    val date: LocalDateTime,
    val name: String,
    val result: String
)

data class PatientInfoResponse(
    @JsonProperty("user_id")
    val userId: Long,
    @JsonProperty("first_name")
    val firstName: String,
    val patronymic: String,
    @JsonProperty("last_name")
    val lastName: String,
    val gender: Int,
    val age: Int,
    val city: String,
    @JsonProperty("receipt_date")
    val receiptDate: LocalDateTime,
    val diagnosis: String,
    @JsonProperty("laboratory_data")
    val laboratoryData: String,
    val features: String?,
    @JsonProperty("allergies_list")
    val allergiesList: List<String>,
    @JsonProperty("researchs_list")
    val researchsList: List<Research>
) : ApiResponse()
