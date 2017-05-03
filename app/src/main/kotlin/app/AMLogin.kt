package app

import android.app.FragmentController
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_amlogin.*
import android.support.design.widget.Snackbar



class AMLogin : Fragment() {



    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?):
            View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_amlogin, container, false)

        login_button.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View): Unit {
                val ammessage = AMMessage()

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, ammessage)
                        .addToBackStack(null)
                        .commit()
            }
        })

    }
}


