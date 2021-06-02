# Pogodynka
Prosta aplikacja na Androida pokazująca aktualną pogodę dla lokazlizacji wskazanej przez użytkownika. Projekt 1 MIM. Zawiera widok uproszczony.

## Funkcje
* Wyświetlanie najważniejszych informacji pogodowych dla wybranego miasta:
  * Aktualna temperatura zmierzona
  * Faktyczna temperatura odczuwalna
  * Piktogram oraz opis pogody
  * Siła i kierunek wiatru
  * Aktualne ciśnienie atmosferyczne
  * Godziny wschodu i zachodu słońca
* Zapisywanie miasta jako domowego
  * Miasto pokazywane przy uruchomieniu aplikacji
* Zapisywanie miast
* Lista ostatnio wyszukiwanych miast
* Widok uproszczony informacji pogodowych

## Użyte technologie
* Kotlin
* Room
* [Retrofit](https://github.com/square/retrofit)
* [Glide](https://github.com/bumptech/glide)

## API
Aplikacja wykorzystuje API udostępnione przez [OpenWeatherMap](https://openweathermap.org/) w celu pobrania aktualnych danych pogodowych.

## Interfejs

| Widok miasta | Widok uproszczony |
|-------------|-------------|
| ![city view](https://user-images.githubusercontent.com/7689591/120460260-403bb700-c399-11eb-9f9d-addee97bd9cf.PNG) | ![city view simple](https://user-images.githubusercontent.com/7689591/120460308-4cc00f80-c399-11eb-80f4-b2f42057587c.PNG) |

| Lista nawigacyjna |
|-------------|
| ![lists view](https://user-images.githubusercontent.com/7689591/120460364-59446800-c399-11eb-80be-9df4ee98ecab.PNG) |

## To Do
* Możliwość usuwania zapisanych miast
* Poprawa nawigacji przy widoku uproszczonym
