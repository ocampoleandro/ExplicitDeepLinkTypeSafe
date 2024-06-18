package com.example.explicitdeeplinktypesafe.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.explicitdeeplinktypesafe.DashboardDestination
import com.example.explicitdeeplinktypesafe.NotificationsDestination
import com.example.explicitdeeplinktypesafe.R
import com.example.explicitdeeplinktypesafe.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
                postNotification()
            } else {
                Toast.makeText(requireContext(), "Notification permission not granted", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        binding.btnDashboard.setOnClickListener {
            findNavController().navigate(DashboardDestination)
        }

        binding.btnDeeplink.setOnClickListener {

            if (ActivityCompat.checkSelfPermission(
                    it.context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                postNotification()
            }

        }

        return root
    }

    @SuppressLint("MissingPermission")
    private fun postNotification() {
        val context = requireContext()
        val pendingIntent = findNavController().createDeepLink()
            .setDestination(
                destRoute = NotificationsDestination.baseRoute
            )
//            .setDestination(
//                destId = NotificationsDestination.serializer().hashCode()
//            ) // this is the workaround as it seems there is no api using type safe???
            .createPendingIntent()

        val notification = NotificationCompat.Builder(context, "1")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle("Notification")
            .setContentText("Notification screen")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(1, notification)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}