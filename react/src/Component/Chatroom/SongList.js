import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Song from './Song'
const styles = {
    
}

const SongList = (props) => {
    const { classes, songs } = props
    return (
        <div>
            {songs.map((song, index) => {
                return (<Song song={song} index={index}/>)
            })}
        </div>
    )
    
}

SongList.propTypes = {
    classes: PropTypes.object.isRequired,
    songs: PropTypes.array.isRequired,
}

export default withStyles(styles)(SongList)