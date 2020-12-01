import React, { useState } from 'react'
import PropTypes from 'prop-types'
import { withStyles } from '@material-ui/core/styles'
import CloseIcon from '@material-ui/icons/Close'
import SongList from './SongList'


const styles = {
    playListDiv: {
        position: 'absolute',
        left: '30%',
        width: '70%',
        height: '100%',
        zIndex: '100',
        backgroundImage: 'linear-gradient(180deg, #2F2A39, #1E2341)',
    },
    playListTitle: {
        fontFamily: 'Sansita Swashed',
        color: 'white',
        marginLeft: '100px',
        marginTop: '60px',
        float: 'left',
    },
    playListHeader: {
        marginLeft: '100px',
        marginTop: '50px',
        color: '#8B8B8B',
        width: '80%',
        borderBottom: '1px solid #8B8B8B',
        paddingBottom: '5px',
    },
    closeIcon: {
        marginTop: '60px',
        marginLeft: '52%',
        fontSize: '40px',
        color: 'white',
    },
}
const formatString = (str, len) => {
    let spaceToAdd = 0
    let strFormat = str
    if (str.length <= len) {
        spaceToAdd = len - str.length
    }
    for (let i = 0; i < spaceToAdd; i++) {
        strFormat = strFormat.concat('\u00a0')
    }
    return strFormat
}
const formatArtist = (artist) => {
    console.log(artist)
    let spaceToAdd = 0
    let artistFormat = artist
    if (artist.length <= 50) {
        spaceToAdd = 50 - artist.length
    }
    for (let i = 0; i < spaceToAdd; i++) {
        artistFormat = artistFormat.concat('\u00a0')
    }
    return artistFormat
}
const PlayList = (props) => {
    const { classes, songs, closePlayList } = props
    console.log(songs)
    return (
        <div className={classes.playListDiv}>
            <h1 className={classes.playListTitle}>John Smith's playlist</h1>
            <CloseIcon className={classes.closeIcon} onClick={closePlayList}/>
            <p className={classes.playListHeader}>{formatString('#', 30)}{formatString('Title', 100)}{formatString('Artist', 50)}</p>
            <SongList songs={songs}/> 
        </div>
    )
    
}

PlayList.propTypes = {
    classes: PropTypes.object.isRequired,
    songs: PropTypes.array.isRequired,
    closePlayList: PropTypes.func.isRequired,
};

export default withStyles(styles)(PlayList)