/*
================================================================================
Edit App.js
================================================================================
*/

import React from 'react';
import './index.css';

// ReactDOM.render(<App />, document.getElementById('root'));
var canvas = document.querySelector("#canvas").getContext("2d");
const size = 10;


function hex(x, y) {
    canvas.beginPath();
    canvas.moveTo(x + size * Math.cos(0), y + size * Math.sin(0));

    for (let side = 0; side < 7; side++) {
        canvas.lineTo(
            x + size * Math.cos(side * 2 * Math.PI / 6),
            y + size * Math.sin(side * 2 * Math.PI / 6)
        );
    }

    // canvas.fillStyle = "#333333";
    // canvas.fill();
    canvas.stroke();
};

const x_offset = 20;
const y_offset = 20;
for (let i = 0; i < 20; i++) {
    const x = i*20 + x_offset;
    const y = 0 + y_offset;
    hex(x, y);
}
