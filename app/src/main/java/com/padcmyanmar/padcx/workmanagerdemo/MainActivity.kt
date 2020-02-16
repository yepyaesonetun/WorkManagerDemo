package com.padcmyanmar.padcx.workmanagerdemo

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.work.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnDownload.setOnClickListener {
            // Create a Constraints object that defines when the task should run
            val downloadConstraints = Constraints.Builder()
                // Device need to charging for the WorkRequest to run.
                .setRequiresCharging(true)
                // Any working network connection is required for this work.
                .setRequiredNetworkType(NetworkType.CONNECTED)
                //.setRequiresBatteryNotLow(true)
                // Many other constraints are available, see the
                // Constraints.Builder reference
                .build()


            // Define the input data for work manager
            val data = Data.Builder()
            data.putString(
                "imageUrl",
                "https://images.freeimages.com/images/large-previews/a3c/maia-2-1436576.jpg"
            )

            // Create an one time work request
            val downloadImageWork = OneTimeWorkRequest
                .Builder(DownloadWorker::class.java)
                .setInputData(data.build())
                .setConstraints(downloadConstraints)
                .build()


            // Enqueue the work
            WorkManager.getInstance(this).enqueue((downloadImageWork))

            // Get the work status using live data
            WorkManager.getInstance(this).getWorkInfoByIdLiveData(downloadImageWork.id)
                .observe(this, Observer { workInfo ->

                    // Toast the work state
                    toast(workInfo.state.name)

                    if (workInfo != null) {
                        if (workInfo.state == WorkInfo.State.ENQUEUED) {
                            // Show the work state in text view
                            textView.text = "Download enqueued."
                        } else if (workInfo.state == WorkInfo.State.BLOCKED) {
                            textView.text = "Download blocked."
                        } else if (workInfo.state == WorkInfo.State.RUNNING) {
                            textView.text = "Download running."
                        }
                    }

                    // When work finished
                    if (workInfo != null && workInfo.state.isFinished) {
                        if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                            textView.text = "Download successful."

                            // Get the output data
                            val successOutputData = workInfo.outputData
                            val uriText = successOutputData.getString("uriString")

                            // If uri is not null then show it
                            uriText?.apply {
                                // If download finished successfully then show the downloaded image in image view
                                imageView.setImageURI(Uri.parse(uriText))
                                textView.text = uriText
                            }
                        } else if (workInfo.state == WorkInfo.State.FAILED) {
                            textView.text = "Failed to download."
                        } else if (workInfo.state == WorkInfo.State.CANCELLED) {
                            textView.text = "Work request cancelled."
                        }
                    }
                })
        }
    }
}
