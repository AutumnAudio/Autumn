import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import { sendChat } from '../AutumnApi/AutumnApi'

const styles = {
    textBox: {
        position: 'absolute',
        left: '30%',
        width: '70%',
        height: '10%',
        top: '90%',
    },
    input: {
        width: '99.6%',
        height: '94%',
        border: '0',
        fontFamily: 'sans-serif',
        // padding: '10px',
    },
}

const sendTextChat = (event, value, setValue) => {
    if (event.key === 'Enter') {
        sendChat(value)
        setValue('')
    }
}
const handleChange = (setValue, event) => {
    setValue(event.target.value);
}
const TextBox = (props) => {
    const { classes } = props
    const [value, setValue] = useState('')
    return (
        <div className={classes.textBox} onKeyUp={(event) => sendTextChat(event, value, setValue)}>
            <textarea  className={classes.input}
              onChange={(event) => handleChange(setValue, event)}
              value={value}/>
        </div>
    )
    
}

TextBox.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(TextBox)