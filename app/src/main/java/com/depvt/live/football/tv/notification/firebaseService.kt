package com.depvt.live.football.tv.notification



////Service class for handling push notifications.....
//class FirebaseService : FirebaseMessagingService() {
//    override fun onMessageReceived(p0: RemoteMessage) {
//        generateNotification(p0)
//    }
//
//    override fun onNewToken(token: String) {
//        super.onNewToken(token)
//    }
//
//    ////generate notification function....
//    private fun generateNotification(remoteMessage: RemoteMessage) {
//
//        val data = remoteMessage.data
//
//        if (data["appname"].equals("com.depvt.live.football.tv", true)) {
//            val notifType = data["type"].toString()
//            if (notifType.equals("PersonalNotification", ignoreCase = true)) {
//                val image = data["image"].toString()
//                if (image.isEmpty()) {
//
//                    sendPersonalNotif(null, data)
//                } else {
//                    getBitmapAsyncAndDoWork(image, data)
//                }
//            }
//        }
//
//    }
//
//    private fun getBitmapAsyncAndDoWork(
//        imageUrl: String,
//        data: Map<String, String>
//    ) {
//        val bitmap = arrayOf<Bitmap?>(null)
//        Glide.with(applicationContext)
//            .asBitmap()
//            .load(imageUrl)
//            .into(object : CustomTarget<Bitmap?>() {
//                override fun onResourceReady(
//                    resource: Bitmap,
//                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap?>?
//                ) {
//                    bitmap[0] = resource
//                    sendPersonalNotif(bitmap[0], data)
//                }
//
//
//                override fun onLoadCleared(placeholder: Drawable?) {
//
//                }
//
//                override fun onLoadFailed(errorDrawable: Drawable?) {
//                    super.onLoadFailed(errorDrawable)
//                    sendPersonalNotif(null, data)
//                }
//
//
//            })
//
//
//    }
//
//    /////if notification type is personal notification....
//    private fun sendPersonalNotif(
//        bitmap: Bitmap?,
//        data: Map<String, String>
//    ) {
//
//        val title = data["title"].toString()
//        val description = data["description"].toString()
//        val url = data["url"].toString()
//        val image_backend = data["image"].toString()
//        val intent = if (url.contains("https://") || url.contains("http://")) {
//            Intent("android.intent.action.VIEW", Uri.parse(url))
//        } else {
//            Intent(this, HomeScreen::class.java)
//        }
//        intent.flags =
//            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            PendingIntent.getActivity(
//                this, 0, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
//            )
//        } else {
//            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
//        }
////        val pendingIntent =
////            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
//        val channelId = "fcm_default_channel"
//        val defaultSoundUri =
//            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val bigText =
//            NotificationCompat.BigTextStyle()
//        bigText.bigText(description)
//        val notificationBuilder =
//            NotificationCompat.Builder(this, channelId)
//                .setStyle(bigText)
//                .setSmallIcon(R.drawable.splash_icon)
//                .setContentTitle(title)
//                .setContentText(description)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent)
//        if (bitmap != null) {
//            notificationBuilder.setStyle(
//                NotificationCompat.BigPictureStyle().bigPicture(bitmap)
//            )
//        }
//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                channelId,
//                "default channel",
//                NotificationManager.IMPORTANCE_HIGH
//            )
//            notificationManager.createNotificationChannel(channel)
//        }
//        val integer: Long = Date().time / 1000L % Int.MAX_VALUE
//        notificationManager.notify(
//            integer.toInt(),
//            notificationBuilder.build()
//        )
//
//        Thread {
//            // do background stuff here
//            saveDataInToDatabase(applicationContext,
//                title,
//                description,
//                url,
//                image_backend)
//
//        }.start()
//
//    }
//
//
//    private fun saveDataInToDatabase(
//        context: Context,
//        title: String,
//        description: String,
//        url: String,
//        image_backend: String
//    ) {
//        val database by lazy { RoomDatabase.getDatabase(context) }
//        database.wordDao().insert(RoomTable(title, description, image_backend, url))
//
//    }
//
//
//
//}