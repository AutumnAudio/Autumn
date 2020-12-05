import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import { addSong } from '../AutumnApi/AutumnApi'
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
        width: '14.5%',
    },
    songTitle: {
        float: 'left',
        // marginLeft: '13.5%',
        width: '47.5%',
        // borderBottom: '1px solid #FFFFFF',
        paddingBottom: '10px'
    },
    songArtist: {
        // marginLeft: '62%',
        float: 'left',
        // borderBottom: '1px solid #FFFFFF',
        paddingBottom: '10px',
        width: '31%',
    },
    addButton: {
        borderRadius: '10px',
        background: 'transparent',
        boxShadow: 'none',
        color: 'white',
        border: '1px solid #707070',
        marginBottom: '10px'
    },
    addButtonDiv: {
        borderBottom: '1px solid #FFFFFF',
        
    }
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
const addCurrentSong = (uri, setIsAdded) => {
    addSong(uri).then((response) => {
        return response.text()
    }).then((text) => {
        console.log(text)
        if (text == 'song added to your queue') {
            setIsAdded(true)
        }
    })
}
const Song = (props) => {
    const { classes, song, index } = props
    const [isAdded, setIsAdded] = useState(false)
    if (song == null) {
        return null
    }
    const uri = song.uri
    
    return (
        
        <div className={classes.songItem}>
            <div className={classes.songNum}>{index + 1}</div>
            <div>
                <div className={classes.songTitle}>{cutString(song.name)}</div>
                <div className={classes.songArtist}>{cutString(song.artists[0])}</div>
                <div className={classes.addButtonDiv}><button className={classes.addButton} onClick={() => addCurrentSong(uri, setIsAdded)}>
                    {isAdded ? ('Added') : ('Add')}
                </button></div>
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