package exam.app.layout


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import exam.app.R


/**
 * A simple [Fragment] subclass.
 */
class AMCreate : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater?,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        /*
        Inflate the layout for this fragment
            Takes the XML code and makes the view.
            ! All buttons should be with fragment
            EX. fragment.BUTTONNAME
         */
        val fragment = inflater!!.inflate(R.layout.fragment_am_create, container, false)



        return fragment
    }

}// Required empty public constructor
