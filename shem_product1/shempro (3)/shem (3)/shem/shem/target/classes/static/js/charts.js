// Ensure Chart.js is loaded in the HTML before this runs
document.addEventListener("DOMContentLoaded", function () {
    const ctx = document.getElementById('energyChart');

    if (ctx) {
        loadChartData(ctx);
    }
});

let myChart = null;

function loadChartData(canvasContext) {
    // 1. Fetch Data from Module 3 API
    fetch('/api/analytics/usage')
        .then(response => {
            if (!response.ok) {
                throw new Error("Network response was not ok");
            }
            return response.json();
        })
        .then(data => {
            // Data format from Java: [ [DeviceId, TotalKwh], [DeviceId, TotalKwh] ]

            // 2. Process Data for Chart.js
            const labels = data.map(item => `Device #${item[0]}`); // X-Axis Labels
            const values = data.map(item => item[1]);              // Y-Axis Values

            // 3. Render Chart
            renderChart(canvasContext, labels, values);
        })
        .catch(error => console.error('Error fetching analytics:', error));
}

function renderChart(ctx, labels, dataPoints) {
    // Destroy old chart if it exists (to prevent overlay bugs on refresh)
    if (myChart) {
        myChart.destroy();
    }

    myChart = new Chart(ctx, {
        type: 'bar', // Change to 'line' or 'doughnut' if you prefer
        data: {
            labels: labels,
            datasets: [{
                label: 'Total Energy Consumed (kWh)',
                data: dataPoints,
                backgroundColor: [
                    'rgba(78, 115, 223, 0.6)',  // Blue
                    'rgba(28, 200, 138, 0.6)',  // Green
                    'rgba(54, 185, 204, 0.6)',  // Teal
                    'rgba(246, 194, 62, 0.6)',  // Yellow
                    'rgba(231, 74, 59, 0.6)'    // Red
                ],
                borderColor: [
                    'rgba(78, 115, 223, 1)',
                    'rgba(28, 200, 138, 1)',
                    'rgba(54, 185, 204, 1)',
                    'rgba(246, 194, 62, 1)',
                    'rgba(231, 74, 59, 1)'
                ],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: true,
                        text: 'Kilowatt Hours (kWh)'
                    }
                }
            },
            plugins: {
                legend: {
                    display: true,
                    position: 'top'
                }
            }
        }
    });
}