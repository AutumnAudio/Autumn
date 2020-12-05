import React, { useEffect } from 'react'
import PropTypes from 'prop-types'
import {
    Link,
    useHistory,
  } from "react-router-dom";

const Home = (props) => {
    const history = useHistory()
    useEffect(() => {
    
        const queryString = window.location.search
        const urlParams = new URLSearchParams(queryString)
        const path = urlParams.get('place')
        console.log(path)
        if (path === 'lobby') {
          history.push('/lobby')
        } else if (path === 'chatroom') {
          const genre = urlParams.get('genre')
          const user = urlParams.get('user')
          history.push('/chatroom', {genre: genre, user: user})
        }
      }, []);
    return (
        <div>
          <Link to='/lobby'>lobby</Link><br />
          <a target="_blank" rel="noreferrer" href='https://accounts.spotify.com/authorize?response_type=code&client_id=17b185367b7d45a8b8cb068eda7787cf&redirect_uri=https%3A%2F%2Fautumn-audio.herokuapp.com%2Fprocess_auth&scope=user-read-email+user-read-recently-played+user-read-currently-playing+playlist-modify-public+playlist-modify-private'>
              Authentication with spotify
          </a>
        </div>
    )
    
}
Home.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default Home