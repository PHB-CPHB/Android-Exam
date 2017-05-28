package exam.app

import java.util.regex.Matcher
import java.util.regex.Pattern

class Validation {
    companion object {
        fun validateEmail(email: String): Boolean {
            val regex: String = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
            val pattern: Pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
            val matcher: Matcher = pattern.matcher(email)
            return matcher.matches()
        }

        fun validatePhonenumber(phonenumber: String): Boolean {
            val regex: String = "^\\d{8}$"
            val pattern: Pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
            val matcher: Matcher = pattern.matcher(phonenumber)
            return matcher.matches()
        }
    }
}