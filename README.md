<!-- PROJECT LOGO -->
<br />
<div align="center">
    <a href="https://github.com/smnprn/yugioh-duel-bot-telegram">
        <img src="images/logo.png" alt="logo" width="80" height="80">
    </a>
    <h2 align="center"><b>Yu-Gi-Oh! Duel Bot</b></h2>
    <p align="center">
        A Telegram bot born to help duelists around the world!
        <br/>
        <a href="https://github.com/smnprn/yugioh-duel-bot-telegram/issues">Report a bug</a>
        -
        <a href="https://github.com/smnprn/yugioh-duel-bot-telegram/issues">Request feature</a>
    </p>
</div>

<!-- TABLE OF CONTENTS -->

<!-- About -->
## About the project
<br/>
<div align="center">

![Bot Preview][preview-image]

</div>

There are many useful apps and websites that help duelists, but they always require to install something on your device and continuously switch from one tool to another. The Yu-Gi-Oh! Duel Bot gives you everything you need in one place without the need to install anything more!

Now you can:
* Search inside the card database.
* Search for card prices.
* Keep count of the life points during the duel.
* Roll a die to choose who go first.
* Have quick access to some of the most popular websites.

And many more features are coming!


### Built with
* ![Java][java-logo]
* ![Telegram][telegram-logo]
* ![REST API][rest-api-logo]
* ![PostgreSQL][postgres-logo]
* ![Redis][redis-logo]

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- Getting Started -->
## Getting started

To start using the bot type @yugiohdueling_bot in the Telegram search bar and then use /start. 

Enjoy!

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- Usage -->
## Usage

The bot currently supports seven commands:
* /start - Shows  a welcome message with links to the official project page, this GitHub repository and Ko-Fi.
* /help - Explains the commands and shows a button to report a bug.
* /database - Searches in the card database. The bot sends a message with the card image and every info on the card.
* /prices - Searches for card prices on TCGPlayer and other marketplaces.
* /roll - Rolls a die.
* /lifepoints - Starts a life points counter. The user can modify the LP by typing "me", "op" or "both" followed by a space and a positive or negative number. 
  
  e.g. "me -1500" reduces the user LP by 1500

* /links - Shows a list with many useful links to other resources such as the official Konami database or Yugipedia.

<br>
</br>

<div align="center">

![Database Function Preview][database-image]

</div>


<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- Roadmap -->
## Roadmap
- [ ] Host the bot
- [x] Add a life points counter
- [x] Add a search card function
- [x] Add card prices

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- Contributing -->
## Contributing
If you wish to contribute to the project <b>feel free to fork the repo and create a pull request</b>!\
You can report a bug opening an "issue" here on GitHub.
Any contribution you make is greatly appreciated.\
 <b>Thank you!</b>

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/YourFeature`)
3. Commit your Changes (`git commit -m 'add: added feature'`)
4. Push to the Branch (`git push origin feature/YourFeature`)
5. Open a Pull Request

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- License -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTACT -->
## Contact

Simone Perna - simoneperna8@gmail.com

If you wish, feel free to write me a message on GitHub!

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- MARKDOWN LINKS & IMAGES -->
[preview-image]: images/preview.png
[java-logo]: https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=coffeescript&logoColor=white
[java-url]: www.java.com
[telegram-logo]: https://img.shields.io/badge/Telegram_API-26A5E4?style=for-the-badge&logo=telegram&logoColor=white
[rest-api-logo]: https://img.shields.io/badge/REST_API-D22128?style=for-the-badge&logo=apache&logoColor=white
[database-image]: images/database.jpg
[postgres-logo]: https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white
[redis-logo]: https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white
