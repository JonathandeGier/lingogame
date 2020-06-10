function processData(data) {
    var dataArray = [];
    data.forEach(function (entry) {
        dataRow = [];

        dataRow.push(entry.player);
        dataRow.push(entry.score);

        dataArray.push(dataRow);
    });
    return dataArray;
}

axios.get("/api/v1/scores")
    .then(response => {
        var scoreData = processData(response.data);

        $(document).ready(function() {
            $('#scoresTable').DataTable({
                data: scoreData,
                columns: [
                    {title: "Naam"},
                    {title: "Score"},
                ]
            });
        } );
});

