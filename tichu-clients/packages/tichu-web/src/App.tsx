import React from 'react';
import logo from './logo.svg';
import './App.css';
import { ReactComponent as SampleCard } from './sample_card.svg';
import {Card, cardFromName} from 'tichu-client-ts-lib';


const CardComp = () => {
    const card :Card = cardFromName('*P');
    console.log(card);
    return (
        <div className="card" style={{width: "18rem"}} draggable>
            <SampleCard/>
            <div className="card-body">
                <h5 className="card-title">{card.name} ({card.shortName})</h5>
            </div>
        </div>
    );
}

function App() {
    return (
        <div className="App">
            <CardComp/>
        </div>
    );
}

export default App;
