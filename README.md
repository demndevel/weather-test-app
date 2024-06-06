# Тестовое задание – приложение для просмотра погоды

API ключ для open weather добавляется в gradle.properties `openWeatherApiKey`.

Три модуля: 

- :app – основное приложение 
- :core – ядро, содержащее модельки и базовые классы
- :data – слой данных: запросы к API и репозиторий

Стек:

- kotlin
- koin
- ktor
- jetpack compose
- kotlinx.serialization
- ViewModel
