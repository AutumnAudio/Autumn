import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';

import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import { withTheme } from '@material-ui/styles';

const styles = {
    paper: {
        height: 180,
        width: 180,
        background: 'pink',
    },
    chatroomName: {
        paddingTop: '75%',
        paddingLeft: '65%',
        fontSize: '25px',
        color: 'white',
    }
};

const ChatroomCard = (props) => {
    const { classes } = props;
    return (
        <Grid item>
            <Paper className={classes.paper}>
                <div className={classes.chatroomName}>Pop</div>
            </Paper>
        </Grid>
    )
    
}
ChatroomCard.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(ChatroomCard)