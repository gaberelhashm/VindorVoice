# Voice Rooms

واجهة Android أولية خفيفة لتطبيق غرف صوتية ودردشة وألعاب، بتصميم بنفسجي داكن.

## البناء المحلي (Termux / Android Studio)

1. انسخ `local.properties.example` إلى `local.properties` وعدّل مسار الـSDK.
2. لو على Termux أضف في `gradle.properties`:
   ```
   android.aapt2FromMavenOverride=/data/data/com.termux/files/usr/bin/aapt2
   ```
3. شغّل:
   ```bash
   chmod +x gradlew
   ./gradlew assembleDebug
   ```
4. الـAPK:
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

## البناء عبر GitHub Actions

1. ارفع المشروع على GitHub (مستودع جديد).
2. اذهب إلى تبويب **Actions**.
3. شغّل الـworkflow **"Build Android APK"** يدويًا أو انتظر بعد أي push على `main`.
4. بعد انتهاء البناء، حمّل الـAPK من قسم **Artifacts** (اسمه `app-debug`).

### رفع المشروع على GitHub (أول مرة)

```bash
cd VoiceRoomsApp
git init
git add .
git commit -m "Initial commit - Voice Rooms app"
git branch -M main
git remote add origin https://github.com/USERNAME/VoiceRoomsApp.git
git push -u origin main
```

استبدل `USERNAME` باسم حسابك.

## المميزات (واجهة Prototype)

- شاشة الغرف الصوتية مع قائمة غرف حية
- شاشة الدردشة
- شاشة الألعاب الجماعية
- شاشة الملف الشخصي
- تصميم Material 3 بنفسجي داكن

## ملاحظة

هذه النسخة هي واجهة التطبيق (UI/prototype) فقط.  
الصوت الحقيقي، الحسابات، الرسائل الفورية، الدفع وVIP تحتاج Backend وخدمة صوتية وقواعد بيانات قبل إطلاق التطبيق كخدمة كاملة.
