package com.wosong.shared_calender

import Model.SavingModel
import Model.UserModel
import Model.dateInfoModel
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.NumberPicker
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Exclude
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale


class Calender : AppCompatActivity() {
    // Firebase에 저장할 데이터의 레퍼런스
    private val selectedDates : MutableList<SavingModel> = mutableListOf()
    private var selectedDate: LocalDate? = null
    private var nickname : String = ""
    private lateinit var database: DatabaseReference

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calender)

        database = FirebaseDatabase.getInstance().reference
        val groupName = intent.getStringExtra("groupName").toString()

        getNickname()



    }
    fun reloadCalendar(){
        //데이터베이스에서 해당 그룹의 selectedDates읽어와서 저장
        //-> 이후에 바인딩해서 해당 날짜에 정보 저장되어 있으면, 달력 색깔 바꿔주는 메서드 작성
        database = Firebase.database.reference
        val caledarView = findViewById<CalendarView>(R.id.calender_view)
        val groupName = intent.getStringExtra("groupName")
        val postReference = database.child("Calendar").child(groupName.toString())
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children){
                    val savinModel = dataModel.getValue(SavingModel::class.java)
                    selectedDates.add(savinModel!!)
                }
                Log.d("saving", selectedDates.toString())
                caledarView.notifyCalendarChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {


            }
        }
        postReference.addValueEventListener(postListener)


    }
    fun byindingCalendar(){
        val caledarView = findViewById<CalendarView>(R.id.calender_view)
        caledarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            @RequiresApi(Build.VERSION_CODES.O)
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            @RequiresApi(Build.VERSION_CODES.O)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.textView.text = data.date.dayOfMonth.toString()
                container.day = data
                val day = data
                val textView = container.textView

                if(day.position == DayPosition.MonthDate){
                    //show the monthDay
                    textView.visibility = View.VISIBLE
                    for(data in selectedDates){
                        if(data.localDate == day.date.toString()){
                            Log.e("saving",data.localDate)
                            Log.e("saving",day.date.toString())
                            textView.setTextColor(Color.RED)
                        }
                    }


                    //checking array

                }else{
                    textView.visibility = View.INVISIBLE
                }

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setUpCalendar(){
        val caledarView = findViewById<CalendarView>(R.id.calender_view)
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(5)  // Adjust as needed
        val endMonth = currentMonth.plusMonths(5)  // Adjust as needed
        val daysOfWeek = daysOfWeek(firstDayOfWeek = DayOfWeek.THURSDAY)

        caledarView.setup(startMonth, endMonth, daysOfWeek.first())
        caledarView.scrollToMonth(currentMonth)

        val titlesContainer = findViewById<ViewGroup>(R.id.titlesContainer)
        titlesContainer.children
            .map { it as TextView }
            .forEachIndexed { index, textView ->
                val dayOfWeek = daysOfWeek[index]
                val title = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                textView.text = title
            }

            caledarView.monthHeaderResource = R.layout.month_header
            caledarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, data: CalendarMonth) {


                    var year = data.yearMonth.year
                    var month = data.yearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())

                    if (container.titlesContainer.tag == null) {
                        container.titlesContainer.tag = data.yearMonth // 연도와 월을 저장하기 위해 tag에 할당합니다.
                        container.titlesContainer.children.map { it as TextView }
                            .forEachIndexed { index, textView ->
                                textView.text = "$month $year"
                            }
                    }
                }

            }
        }//end of setup
    @RequiresApi(Build.VERSION_CODES.O)
    fun showDialog() : Boolean {
        val mDialog = LayoutInflater.from(this).inflate(R.layout.calendar_dialog, null)
        val mBulider = AlertDialog.Builder(this)
            .setView(mDialog)
            .setTitle("Saving info")
        val mAlertDialog = mBulider.show()
        //dialog size adjustment
        val window = mAlertDialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        val btn = mAlertDialog.findViewById<Button>(R.id.CalenderBtn)
        val hour = mAlertDialog.findViewById<NumberPicker>(R.id.hourPicker)
        hour!!.minValue = 0
        hour!!.maxValue = 24
        val minute = mAlertDialog.findViewById<NumberPicker>(R.id.minutePicker)
        minute!!.minValue = 0
        minute!!.maxValue = 5
        val displayedValues = Array(6) { index -> (index * 10).toString() }
        minute.displayedValues = displayedValues
        minute.setOnValueChangedListener { picker, oldVal, newVal ->
            val newValue = newVal * 10
        }

        val info = mAlertDialog.findViewById<EditText>(R.id.CalenderText)
        val groupName = intent.getStringExtra("groupName")
        database = Firebase.database.reference

        btn!!.setOnClickListener {
                //다른날짜를 클릭했다면, 뷰 컨테이너 클래스에서 selectedDate가 업데이트 되었을 것이기 때문에
                //이차원 배열에 날짜랑 정보를 쌍으로 저장.

                val savingModel = SavingModel(selectedDate.toString(),nickname,hour.value.toString(), (minute.value*10).toString(), info!!.text.toString())

                selectedDates.add(savingModel)

                database.child("Calendar").child(groupName.toString()).push().setValue(savingModel)



                mAlertDialog.dismiss()
             }
            return true
        }//end of showDialog()
    fun getNickname(){
        database = Firebase.database.reference
        val uid = Firebase.auth.currentUser?.uid

        if(uid != null){
            val postReference = database.child("userModel").child(uid)

            postReference.addListenerForSingleValueEvent(object : ValueEventListener{
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userModel = snapshot.getValue(UserModel::class.java)
                    userModel?.let {
                        nickname = userModel.nickname
                        Log.e("nickname", nickname)


                        setUpCalendar()

                        reloadCalendar()

                        byindingCalendar()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun showInfoDialog(){
        val mDialog = LayoutInflater.from(this).inflate(R.layout.info_dialog,null)
        val mBulider = AlertDialog.Builder(this)
            .setView(mDialog)
            .setTitle("show date information")
        val mAlertDialog = mBulider.show()
        //dialog size adjustment
        val window = mAlertDialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)

        //뷰 컨테이너에서 선택한 날짜를 저장시켰으므로 dialog에서 바로 띄워주기 가능
        val date = mAlertDialog.findViewById<TextView>(R.id.DateInfo)
        date!!.text = selectedDate.toString()

        //현재 선택된 날짜에 있는 정보들만 새 리스트에 저장
        val inflateInfo : MutableList<SavingModel> = mutableListOf()
        for(data in selectedDates){
            //현재 선택한 날짜의 데이터
            //데이터중복 방지를 위해 저장하려고 하는 데이터가 inflateInfoList에 포함되어 있지 않다면
            if(data.localDate == selectedDate.toString() && !inflateInfo.contains(data)){
                inflateInfo.add(data)
            }
        }


        val listView = mAlertDialog.findViewById<ListView>(R.id.dialog_list)
        val adapter = infoAdapter(baseContext, inflateInfo)
        listView!!.adapter = adapter



    }

    inner class MonthViewContainer(view: View) : ViewContainer(view) {
        // Alternatively, you can add an ID to the container layout and use findViewById()
        val titlesContainer = view as ViewGroup

    }

    @RequiresApi(Build.VERSION_CODES.O)
    inner class DayViewContainer(view: View) : ViewContainer(view) {
        val textView = view.findViewById<TextView>(R.id.tv_day)
        val calenderView = view.findViewById<CalendarView>(R.id.calender_view)
        lateinit var day: CalendarDay


        init {
            view.setOnLongClickListener {
                if(day.position == DayPosition.MonthDate){
                    //checking current month
                    //keeping Reference
                    selectedDate = day.date
                }
                showDialog()
            }
           view.setOnClickListener {
               if(day.position == DayPosition.MonthDate){
                   selectedDate = day.date
               }
               showInfoDialog()
           }
        }
    }
    }//end of class



