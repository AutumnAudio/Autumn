import React, { useState, useEffect } from 'react';
import logo from './logo.svg';
import './App.css';
import Chatroom from './Component/Chatroom/Chatroom'
import Lobby from './Component/Lobby/Lobby'
import Home from './Home'
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link,
  useRouteMatch,
  useParams,
} from "react-router-dom";

function App() {
  return (
    <Router>
      
      <Switch>
        <Route path="/chatroom">
          <Chatroom />
        </Route>
      </Switch>
      <Switch>
        <Route path="/lobby">
          <Lobby />
        </Route>
      </Switch>
      <Switch>
        <Route path="/">
          <Home />
        </Route>
      </Switch>
    </Router>
  );
}

export default App;
