// BarChart.js
import React, { useEffect, useRef } from 'react';
import Chart from 'chart.js/auto';

const BarChart = ({ data, XaxisLabel, YaxisLabel, barColor }) => {
  const chartRef = useRef(null);

  useEffect(() => {
    const ctx = chartRef.current.getContext('2d');

    // Check if there's an existing chart instance
    if (chartRef.current && chartRef.current.chart) {
      // Destroy the existing chart
      chartRef.current.chart.destroy();
    }

    // Create a new chart instance
    const newChart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: data.labels,
        datasets: [
          {
            label: 'Bar Chart',
            data: data.values,
            backgroundColor: barColor ? barColor : 'rgba(75, 192, 192, 0.8)', // Bar color
            borderColor: 'rgba(75, 192, 192, 1)', // Border color
            borderWidth: 1,
          },
        ],
      },
      options: {
        scales: {
          x: {
            type: 'category',
            title: {
              display: true,
              text: XaxisLabel,
            },
          },
          y: {
            beginAtZero: true,
            title: {
              display: true,
              text: YaxisLabel,
            },
          },
        },
      },
    });

    // Save the chart instance in the ref
    chartRef.current.chart = newChart;

  }, [data]);

  return <canvas ref={chartRef} />;
};

export default BarChart;
