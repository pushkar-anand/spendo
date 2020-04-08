package me.pushkaranand.spendo.ui.fragments

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_bottomsheet.*
import me.pushkaranand.spendo.R
import me.pushkaranand.spendo.ui.CategoryDisplayActivity
import me.pushkaranand.spendo.ui.SettingsActivity
import me.pushkaranand.spendo.ui.activities.home.HomeActivity

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bottomsheet, container, false)
        //return MenuBinding.inflate(inflater, container, false).root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bottomNavView.setNavigationItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                R.id.nav_transactions -> {
                    val intent = Intent(context, HomeActivity::class.java)
                    context!!.startActivity(intent)
                }
                R.id.nav_settings -> {
                    val intent = Intent(context, SettingsActivity::class.java)
                    context!!.startActivity(intent)
                }
                R.id.nav_categories -> {
                    val intent = Intent(context, CategoryDisplayActivity::class.java)
                    context!!.startActivity(intent)
                }
                R.id.nav_feedback -> {

                    try {
                        val uri = Uri.parse("market://details?id=${context?.packageName}")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                        Toast.makeText(
                            context,
                            "Please rate the app stars. If possible write a review",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    } catch (e: ActivityNotFoundException) {

                        Toast.makeText(
                            context,
                            "Error opening play store",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                }
                R.id.nav_share -> {

                    try {
                        val shareMsg = "${getString(R.string.share_text)} ${getString(R.string.share_link)}"
                        val shareIntent = Intent()
                        shareIntent.action = Intent.ACTION_SEND
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMsg)
                        shareIntent.type = "text/plain"
                        startActivity(shareIntent)
                    } catch (e: ActivityNotFoundException) {

                        Toast.makeText(
                            context,
                            "No sharing apps found on phone.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
            true
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme)
}