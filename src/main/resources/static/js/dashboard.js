
$(document).ready(function(){
 
    var current_url = window.location.href; // http://localhost:8080/...
    var api = "http://" + current_url.split("/")[2] + "/api/periode/listPeriode";
    var listLabel = [];
    var listData = [];

    $.getJSON(api, function (data) {
        $.each( data, function( key, val ) {
            listLabel.push(key);
            listData.push(val);
        });

        $("#periodeAwalSelector").val(listLabel[0]);
        $("#periodeAkhirSelector").val(listLabel[listLabel.length - 1]);
        console.log(listLabel[listLabel.length - 1] + " v")


        var lineGraph= document.getElementById('myChart').getContext('2d');
        var chart = new Chart(lineGraph, {
            // The type of chart we want to create
            type: 'line',
        
            // The data for our dataset
            data: {
                labels: listLabel,
                datasets: [{
                    label: 'Capaian Penilaian',
                    backgroundColor: 'rgba(0,116,207,0.15)',
                    borderColor: '#0072BB',
                    data: listData,
                    fill:true,
                    lineTension: 0,
                    display:true,
                    datalabels: {
                        align: 'end',
                        anchor: 'end'
                    }
                },
                ]
            },
        
            // Configuration options go here
            options: {
                legend: {
                    display: false,
                },
                scales: {
                    yAxes: [{
                      scaleLabel: {
                        display: true,
                        labelString: 'capaian (%)',
                        fontColor: '#0072BB',
                        fontStyle: "bold",
                      },
                      ticks:{
                          suggestedMin: 0,
                          stepValue: 2,
                          max: 100
                      }
                    }],
                    xAxes: [{
                        scaleLabel: {
                          display: true,
                          labelString: 'periode penilaian',
                          fontColor: '#0072BB',
                          fontStyle: "bold"
                        }
                      }]
                },
                plugins: {
                    datalabels: {
                        backgroundColor: function(context) {
                            return context.dataset.borderColor;
                        },
                        borderRadius: 4,
                        color: 'white',
                        font: {
                            weight: 'bold'
                        },
                        formatter: Math.round
                    }
                },
            }
        });

        var barGraph= document.getElementById('CapaianTahunanBar').getContext('2d');
        var myBarChart = new Chart(barGraph, {
            type: 'horizontalBar',
            data: {
                labels: listLabel,
                datasets: [{
                    label: 'Capaian Penilaian',
                    backgroundColor: '#F16522',
                    borderColor: '#F16522',
                    data: listData,
                    fill:true,
                },
                ]   
            },
            options: {
                title:{
                    display: true,
                    text: 'Capaian penilaian tahunan (%)',
                    fontColor: '#0072BB',
                    fontStyle: "bold",
                },
                legend: {
                    display: false,
                },
                scales: {
                    xAxes: [{
                        display: false,
                        stacked:true,
                        ticks:{
                            suggestedMin: 0,
                            stepValue: 2,
                            max: 100
                        }
                    }]
                },
                plugins: {
                    datalabels: {
                        backgroundColor: function(context) {
                            return context.dataset.borderColor;
                        },
                        borderRadius: 5,
                        color: 'white',
                        anchor: 'end',
                        align: 'end',
                        font: {
                            size: '11',
                            weight: '600'
                        }
                    }
                  }
            }
        });

        $(".btn-view-periode").click(function (event) {
            var oldLabel = listLabel;
            var oldData = listData;

            var newLabel = [];
            var newData = [];

            console.log(oldLabel," -old data- ",oldData);

            var periode_awal = $("#periodeAwalSelector").val();
            var periode_akhir = $("#periodeAkhirSelector").val();
            console.log(periode_awal," - ",periode_akhir);

            oldLabel.forEach(function (currentPeriod, index){
                if (!(currentPeriod<periode_awal || currentPeriod>periode_akhir)){
                    newLabel.push(currentPeriod);
                    newData.push(oldData[index]);
                }
            });

            if (periode_akhir < periode_awal){
                $("#alertModal").modal("show");
                console.log("terjadi kesalahan")
            }

            else{
                chart.data.datasets[0].data = newData;
                chart.data.labels = newLabel;
                chart.update();

                myBarChart.data.datasets[0].data = newData;
                myBarChart.data.labels = newLabel;
                myBarChart.update();

                console.log(newLabel," -new data- ",newData);
            }


        });
    });



    
});





function getLabel() {
    var curerent_url = window.location.href; // http://localhost:8080/...
    var api = "http://" + curerent_url.split("/")[2] + "/api/periode/listPeriode";
    var listLabel = [];
    $.getJSON(api, function (data) {
        $.each( data, function( key, val ) {
            listLabel.push(key)
        });
    });
    return listLabel;
}






