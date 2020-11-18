import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';

const styles = {
    textBox: {
        position: 'absolute',
        left: '30%',
        width: '70%',
        height: '10%',
        top: '90%',
    },
    input: {
        
        width: '100%',
        height: '100%',
        border: '0',
        fontFamily: 'sans-serif',
        // padding: '10px',
    }
};

const TextBox = (props) => {
    const { classes } = props;
    return (
        <div className={classes.textBox}>
            <textarea  className={classes.input}/>
        </div>
    )
    
}

TextBox.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(TextBox)