import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import PersonIcon from '@material-ui/icons/Person';
import AddIcon from '@material-ui/icons/Add';

const styles = {
    personIcon: {
        position: 'relative',
        marginLeft: '10%',
        float: 'left',
        fontSize: '40px'
    },
    nameDiv: {
        positive: 'relative',
        marginLeft: '25%',
        fontSize: '20px',
    },
    currentSongDiv: {
        fontSize: '12px',
        position: 'relative',
        marginLeft: '25%',
        color: '#D0D1D7',
    },
    personDiv: {
        color: 'white',
        margin: '20px',
    },
    addIcon: {
        position: 'absolute',
        marginLeft: '270px',
        marginTop: '-10px',
    }
};

const Person = (props) => {
    const { classes, person, updatePlayListSongs } = props
    var recentlyPlayedSong = ''
    var recentlyPlayedArtist = ''
    var recentlyPlayedSongs = person['recentlyPlayed']
    if (person['recentlyPlayed'][0] != null) {
        recentlyPlayedSong = person['recentlyPlayed'][0]['name']
        recentlyPlayedArtist = person['recentlyPlayed'][0]['artists'][0]
    }
    return (
        <div className={classes.personDiv}>
            <PersonIcon className={classes.personIcon}/>
            <div className={classes.nameDiv}>{person.username}</div>
            <AddIcon className={classes.addIcon}/>
            <div className={classes.currentSongDiv} onClick={() => updatePlayListSongs(recentlyPlayedSongs)}>
                {recentlyPlayedSong} - {recentlyPlayedArtist}
            </div>
        </div>
    )
    
}

Person.propTypes = {
    classes: PropTypes.object.isRequired,
    person: PropTypes.object.isRequired,
    updatePlayListSongs: PropTypes.func.isRequired,
};

export default withStyles(styles)(Person)