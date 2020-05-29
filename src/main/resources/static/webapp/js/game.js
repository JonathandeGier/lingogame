var gameId;
var initialFeedback;
var givenFeedbacks;

var guessBlock = document.getElementById('guessBlock');
var nextRoundBlock = document.getElementById('nextRoundBlock');
var gameOverBlock = document.getElementById('gameOverBlock');
var saveScoreBlock = document.getElementById('saveScoreBlock');

var messageBlock = document.getElementById('messageBlock');
var scoreLabel = document.getElementById('scoreLabel');

document.getElementById("guessInput").addEventListener("keypress", function (ev) {
    if (ev.key === "Enter") {
        guess();
    }
});


function init() {
    axios.post('/api/v1/game')
        .then(response => {
            gameId = response.data.gameId;
        })
        .catch(error => console.error(error));

    buildEmptyTable(5, 5);
    displayScore(0);
}

function startRound() {
    axios.post('/api/v1/game/' + gameId + '/round')
        .then(response => {
            initialFeedback = response.data;
            givenFeedbacks = [];

            buildTable();
            showGuessBlock();
        })
        .catch(error => console.error(error));
}

function guess() {
    var input = document.getElementById("guessInput");
    var guess = input.value.toLowerCase();

    axios.post('/api/v1/game/' + gameId + '/guess', {guess: guess})
        .then(response => {
            givenFeedbacks.push(response.data);

            buildTable();
            displayMessage(response.data.explaination);
            input.value = "";

            if (response.data.explaination === 'CORRECT') {
                showNextRoundBlock();
                updateScore();
            } else if (response.data.explaination === 'GAME_OVER') {
                showGameOverBlock();
            }
        })
        .catch(error => console.error(error));
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
                } else if (givenFeedbacks[i - 1] !== undefined && givenFeedbacks[i - 1].explaination !== "CORRECT") {
                    // display correct letters of last feedback
                    if (j === 0) {
                        cell.innerHTML = initialFeedback.guess.charAt(0).toUpperCase();
                    } else {
                        for(k = 0; k < i; k++) {
                            if (givenFeedbacks[k].feedback[j] !== undefined && givenFeedbacks[k].feedback[j].feedbackType === "CORRECT") {
                                cell.innerHTML = givenFeedbacks[k].feedback[j].letter.toUpperCase();
                            }
                        }
                    }
                }
            }
        }
    }
}

function updateScore() {
    axios.get('/api/v1/game/' + gameId + '/score')
        .then(response => {
            displayScore(response.data.score);
        })
        .catch(error => console.error(error));
}

function saveScore() {
    var playerName = document.getElementById("playerNameInput").value;

    axios.post('/api/v1/game/' + gameId + '/score', {name: playerName})
        .then(response => {
            window.location.href = "/highscores";
        })
        .catch(error => console.error(error));
}

function displayScore(score) {
    scoreLabel.innerHTML = score;
}


function showGuessBlock() {
    guessBlock.style.display = 'block';
    nextRoundBlock.style.display = 'none';
    gameOverBlock.style.display = 'none';
    saveScoreBlock.style.display = 'none';
}

function showNextRoundBlock() {
    guessBlock.style.display = 'none';
    nextRoundBlock.style.display = 'block';
    gameOverBlock.style.display = 'none';
    saveScoreBlock.style.display = 'none';
}

function showGameOverBlock() {
    guessBlock.style.display = 'none';
    nextRoundBlock.style.display = 'none';
    gameOverBlock.style.display = 'block';
    saveScoreBlock.style.display = 'none';
}

function showSaveScoreForm() {
    guessBlock.style.display = 'none';
    nextRoundBlock.style.display = 'none';
    gameOverBlock.style.display = 'none';
    saveScoreBlock.style.display = 'block';
}

function displayMessage(message) {
    messageBlock.innerHTML = explainationToString(message);
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

function explainationToString(explaination) {
    return strings[explaination];
}

var strings = {
    GOOD_GUESS: "",
    OUT_OF_TIME: "Buiten de tijd",
    WORD_DOES_NOT_EXIST: "Woord bestaat niet",
    WORD_TOO_SHORT: "Woord is te kort",
    WORD_TOO_LONG: "woord is te lang",
    CORRECT: "",
    GAME_OVER: "Game Over",
};