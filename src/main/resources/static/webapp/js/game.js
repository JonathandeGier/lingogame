var initialFeedback;

var givenFeedbacks;

document.getElementById("guessInput").addEventListener("keypress", function (ev) {
    if (ev.key === "Enter") {
        guess();
    }
});

function init() {
    // TODO: api request to start a new game

    buildEmptyTable(5, 5);


}

function startRound() {
    // TODO: api request to start a new round and fill initialFeedback and empty givenFeedbacks

    initialFeedback = {
        guess: "w",
        feedback: [
            {
                letter: "w",
                feedbackType: "CORRECT"
            }
        ],
        wordLength: 5,
        totalGuesses: 5,
        guessesLeft: 5
    };

    givenFeedbacks = [];

    buildTable();
}

function guess() {
    var input = document.getElementById("guessInput");
    var guess = input.value;

    // TODO: api request to guess

    givenFeedbacks.push({guess: guess});
    buildTable()
    input.value = "";
}

function buildTable() {
    var table = document.getElementById("game");

    // clear the table
    while (table.hasChildNodes()) {
        table.removeChild(table.firstChild);
    }

    for (i = 0; i < initialFeedback.totalGuesses; i++) {
        var row = table.insertRow(-1);

        for (j = 0; j < initialFeedback.wordLength; j++) {
            cell = row.insertCell();
            cell.classList.add("lingo-cell");

            if (givenFeedbacks[i] !== undefined) {
                // feedback for this guess has been given, so display it
                cell.innerHTML = givenFeedbacks[i].guess.charAt(j).toUpperCase();
                if (givenFeedbacks[i].feedback[j] !== undefined) {
                    if (givenFeedbacks[i].feedback[j].feedbackType === "CORRECT") {
                        cell.classList.add("correct")
                    } else if (givenFeedbacks[i].feedback[j].feedbackType === "PRESENT") {
                        cell.classList.add("present")
                    }
                }
            } else {
                if (i === 0 && initialFeedback.feedback[j] !== undefined && initialFeedback.feedback[j].feedbackType === "CORRECT") {
                    // no given feedback on the first guess means the game has just started, so display the initial feedback
                    cell.innerHTML = initialFeedback.guess.charAt(j).toUpperCase();
                } else if (givenFeedbacks[i - 1] !== undefined && givenFeedbacks[i - 1].feedback[j] !== undefined && givenFeedbacks[i - 1].feedback[j].feedbackType === "CORRECT") {
                    // display correct letters of last feedback
                    cell.innerHTML = givenFeedbacks[i - 1].guess.charAt(j).toUpperCase();
                }
            }
        }
    }
}

function buildEmptyTable(wordLength, guesses) {
    var table = document.getElementById("game");

    // clear the table
    while (table.hasChildNodes()) {
        table.removeChild(table.firstChild);
    }

    for (i = 0; i < guesses; i++) {
        var row = table.insertRow(-1);

        for (j = 0; j < wordLength; j++) {
            cell = row.insertCell();
            cell.classList.add("lingo-cell");
        }
    }
}