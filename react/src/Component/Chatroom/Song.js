import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';

const styles = {
    songItem: {
        marginLeft: '100px',
        color: '#8B8B8B',
        width: '80%',
        paddingBottom: '5px',
        marginTop: '10px',
        color: 'white',
    },
    songNum: {
        float: 'left',
    },
    songTitle: {
        float: 'left',
        marginLeft: '13.5%',
        width: '47.5%',
        borderBottom: '1px solid #FFFFFF',
        paddingBottom: '10px'
    },
    songArtist: {
        marginLeft: '62%',
        borderBottom: '1px solid #FFFFFF',
        paddingBottom: '10px'
    },
}
const formatString = (str, len) => {
    let spaceToAdd = 0
    let strFormat = str
    if (str.length <= len) {
        spaceToAdd = len - str.length
    }
    console.log(spaceToAdd)
    for (let i = 0; i < spaceToAdd; i++) {
        strFormat = strFormat.concat('\u00a0')
    }
    return strFormat
}
const cutString = (str) => {
    if (str.length > 50) {
        return str.substring(0, 47).concat('...')
    } else {
        return str
    }
}
const Song = (props) => {
    const { classes, song, index } = props
    if (song == null) {
        return null
    }
    return (
        
        <div className={classes.songItem}>
            <div className={classes.songNum}>{index + 1}</div>
            <div>
                <div className={classes.songTitle}>{cutString(song.name)}</div>
                <div className={classes.songArtist}>{cutString(song.artists[0])}</div>
            </div>
            
        </div>
    )
    
}

Song.propTypes = {
    classes: PropTypes.object.isRequired,
    song: PropTypes.object.isRequired,
    index: PropTypes.number.isRequired,
}

export default withStyles(styles)(Song)