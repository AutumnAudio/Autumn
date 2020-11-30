import React from 'react';
import './App.css';
import Chatroom from './Component/Chatroom/Chatroom'
import Lobby from './Component/Lobby/Lobby'
import {
  BrowserRouter as Router,
  Switch,
  Route,
} from "react-router-dom";

function App() {
  return (
    <Router>
      <Switch>
        <Route path="/">
          <Lobby />
        </Route>
      </Switch>
      <Switch>
        <Route path="/chatroom">
          <Chatroom />
        </Route>
      </Switch>
      
    </Router>
  );
}

export default App;
