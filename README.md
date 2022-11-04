# WaracleCakes

Waracle Cakes client app showcasing MVVM and clean architecture.

This application fetches list of cakes from backend and displays it on a recycle view.

Tech and libraries used: Kotlin, Coroutine, Koin, Retrofit, GSON, Glide, MockK

MVVM design pattern and utilised Android Architecture Components, such as ViewModel.

Package modularisation for clean code structure, DATA (Repositories), DOMAIN(Use cases) and APP(UI).

Kept UI to minimal.

Added unit test using MockK on repository, usecases and view model.

## TODO

Due to time constrain, not done the following:

* Network availability check
* Remove few hardcoded dimens/color values
* Add styles and attributes to support light/dark mode
* Custom error handling and show error using error screen, currently using simple Snackbar
* Optimise recycle view update with DiffUtil
* Splash screen using Launcher Theme
* UI test using Espresso
