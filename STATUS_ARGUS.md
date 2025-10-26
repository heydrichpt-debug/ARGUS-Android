# ARGUS — Checkpoint (para retomada em novo chat)

## Resumo rápido
- Projeto Android (Kotlin), repositório: `heydrichpt-debug/ARGUS-Android`, branch `main`.
- CI: GitHub Actions em `.github/workflows/android.yml` (build do APK Debug).
- SDKs: compileSdk/targetSdk = 34, minSdk = 24.
- Gradle 8.7, AGP 8.5.2, Kotlin 1.9.24.
- App **compila** mas em algumas execuções o CI não gera APK (problemas pontuais/instabilidade de cache do Actions). Últimos ajustes focaram em Manifest, regex, packaging, e Network Security Config para HTTP.

## Mudanças feitas no app (principais)
- `AndroidManifest.xml`: `android:exported="true"` em `MainActivity`.  
- `AdminScraper.kt`: correção de escapes em regex (substituição de `\d` -> `\\d` e `\.` -> `\\.`).  
- `build.gradle.kts (app)`:  
  - `compileSdk = 34`, `targetSdk = 34`, Java/Kotlin 17.  
  - Adicionadas libs alinhadas ao SDK 34 (`activity-ktx 1.9.2`, `fragment-ktx 1.7.1`).  
  - `packaging { resources { excludes ... } }` para evitar conflitos META-INF (okhttp/jsoup).  
- `gradle.properties`: `android.useAndroidX=true`, `android.enableJetifier=true`, `org.gradle.jvmargs=-Xmx2g -Dfile.encoding=UTF-8`.  
- **Network Security (temporário para testes)**:
  - `res/xml/network_security_config.xml` permitindo **HTTP** para `divaneural.com`.  
  - `strings.xml`: `default_api_base` temporariamente ajustado para `http://divaneural.com/api`.  
  > Em produção, **voltar para HTTPS** e remover essa exceção.

## Pastas/arquivos no seu telefone
- Raiz do projeto: `~/argus_android`
- Manifests/Res:
  - `app/src/main/AndroidManifest.xml`
  - `app/src/main/res/values/strings.xml`
  - `app/src/main/res/xml/network_security_config.xml`
- Códigos:
  - `app/src/main/java/com/divaneural/argus/...`

## CI (GitHub Actions)
- Workflow: `.github/workflows/android.yml`  
  Passos principais:  
  1) `actions/setup-java@v4` (Temurin 17)  
  2) `android-actions/setup-android@v3`  
  3) `sdkmanager` instala `platforms;android-34` e `build-tools;34.0.0`  
  4) `gradle wrapper --gradle-version 8.7`  
  5) `chmod +x ./gradlew`  
  6) `./gradlew :app:assembleDebug`  
  7) Upload do APK de `app/build/outputs/apk/debug/*.apk`  

Para disparar uma build:  
```bash
git commit --allow-empty -m "ci: trigger build" && git push
md
