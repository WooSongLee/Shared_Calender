package Model

import android.provider.ContactsContract.CommonDataKinds.Nickname
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.time.LocalDate

@IgnoreExtraProperties
data class SavingModel(
    val localDate: String? = null,
    val nickname: String,
    val hour : String,
    val minute : String,
    val info : String
){
    constructor() : this("","","","","")
}