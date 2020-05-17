var feedbacks = [
    {
        guess: "h",
        wordLength: 5,
        totalGuesses: 5,
        guessesLeft: 5
    },
    {
        guess: "hello",
        wordLength: 5,
        totalGuesses: 5,
        guessesLeft: 4
    },
    {
        guess: "helloas",
        wordLength: 5,
        totalGuesses: 5,
        guessesLeft: 3
    }
];

document.getElementById("guessInput").addEventListener("keypress", function (ev) {
    if (ev.key === "Enter") {
        guess();
    }
});

function init() {
    // TODO: api request to start a new game

    buildTable();
}

function buildTable() {
    var table = document.getElementById("game");

    // clear the table
    while (table.hasChildNodes()) {
        table.removeChild(table.firstChild);
    }

    for (i = 0; i < feedbacks[0].totalGuesses; i++) {
        var row = table.insertRow(-1);

        for (j = 0; j < feedbacks[0].wordLength; j++) {
            cell = row.insertCell();
            cell.classList.add("lingo-cell");

            if (feedbacks[i] !== undefined) {
                cell.innerHTML = feedbacks[i].guess.charAt(j).toUpperCase();
            }
        }
    }
}

function guess() {
    var input = document.getElementById("guessInput");
    var guess = input.value;
    feedbacks.push({guess: guess});
    buildTable()
    input.value = "";
}