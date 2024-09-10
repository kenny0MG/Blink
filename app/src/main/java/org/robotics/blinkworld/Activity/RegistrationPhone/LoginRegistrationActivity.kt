package org.robotics.blinkworld.Activity.RegistrationPhone

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import org.robotics.blinkworld.R
import org.robotics.blinkworld.Utils.NODE_USERS
import org.robotics.blinkworld.Utils.currentUid
import org.robotics.blinkworld.Utils.database

class LoginRegistrationActivity : AppCompatActivity() {

    private lateinit var continueButton: AppCompatButton
    private lateinit var nameTextView:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_registration)
        //инициализация объектов активити (поиск по id)
        continueButton = findViewById(R.id.login_reg_continue_button)
        nameTextView = findViewById(R.id.name_login_text_view)


        nameTextView.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
               if(p0!!.length >= 30){
                   Toast.makeText(applicationContext , "Max 30 symbol" , Toast.LENGTH_SHORT).show()
               }

            }

        }

        )




        //обработчик нажатий
        continueButton.setOnClickListener {
            if(nameTextView.text.isEmpty()){
                Toast.makeText(this , "Write name" , Toast.LENGTH_SHORT).show()

            }else{
                database.child(NODE_USERS).orderByChild("username").equalTo(nameTextView.text.toString()).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.getValue() != null){
                            nameTextView?.error ="This name is already in use"
                        }else{
                            val intent = Intent(this@LoginRegistrationActivity, RegistrationPhoneActivity::class.java)
                            intent.putExtra("username",nameTextView.text.toString())
                            startActivities(arrayOf(intent))
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("TAG", "onVerificationFailed: ${error.toString()}")
                    }
                }
                )

            }

        }
        nameTextView.requestFocus()



    }
}