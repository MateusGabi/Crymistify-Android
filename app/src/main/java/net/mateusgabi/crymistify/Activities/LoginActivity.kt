package net.mateusgabi.crymistify.Activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import net.mateusgabi.crymistify.R


class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private val TAG = "LoginActivity"

    private val RC_SIGN_IN = 9001
    private var mAuth: FirebaseAuth? = null

    private var mGoogleSignInClient: GoogleSignInClient? = null

    private var mStatusTextView: TextView? = null
    private var mDetailTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        // Views
        mStatusTextView = findViewById(R.id.status)
        mDetailTextView = findViewById(R.id.detail)

        // Button listeners
        findViewById<View>(R.id.sign_in_button).setOnClickListener(this)
        findViewById<View>(R.id.sign_out_button).setOnClickListener(this)
        findViewById<View>(R.id.disconnect_button).setOnClickListener(this)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mAuth = FirebaseAuth.getInstance()
    }


    override fun onStart() {
        super.onStart()

        val firebaseUser: FirebaseUser? = mAuth?.currentUser
        updateUI(firebaseUser)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode === RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                updateUI(null)
            }

        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account?.id)
        showProgressDialog()

        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        mAuth?.signInWithCredential(credential)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth?.currentUser
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success ${user?.getIdToken(true)}")
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.exception)
                        Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                        updateUI(null)
                    }

                    hideProgressDialog()
                }
    }

    override fun onClick(v: View?) {
        val i = v?.id
        if (i == R.id.sign_in_button) {
            signIn()
        } else if (i == R.id.sign_out_button) {
            signOut()
        } else if (i == R.id.disconnect_button) {
            revokeAccess()
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        hideProgressDialog()
        if (user != null) {

            val intent: Intent = Intent(this, Main2Activity::class.java)
            startActivity(intent)
            finish()

            mStatusTextView?.text = user.email
            mDetailTextView?.text = user.uid

            findViewById<View>(R.id.sign_in_button).setVisibility(View.GONE)
            findViewById<View>(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE)
        } else {
            mStatusTextView?.text = "Sair"
            mDetailTextView?.text = null

            findViewById<View>(R.id.sign_in_button).visibility = (View.VISIBLE)
            findViewById<View>(R.id.sign_out_and_disconnect).visibility = (View.GONE)
        }
    }

    private fun revokeAccess() {
        // Firebase sign out
        mAuth?.signOut()

        // Google revoke access
        mGoogleSignInClient?.revokeAccess()?.addOnCompleteListener(this
        ) { updateUI(null) }
    }

    private fun signOut() {
// Firebase sign out
        mAuth?.signOut()

        // Google sign out
        mGoogleSignInClient?.signOut()?.addOnCompleteListener(this
        ) { updateUI(null) }
    }

    private fun signIn() {

        var signInIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun showProgressDialog() {

    }

    private fun hideProgressDialog() {
    }

}
