import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import Grid from '@material-ui/core/Grid';
import ChatroomCard from './ChatroomCard';

const styles = {
    root: {
        width: '100%',
        marginLeft: '0',
        marginTop: '40px',
    }
};

const ChatroomCardGrid = (props) => {
    const { classes } = props;
    return (
        <div>
            <Grid container className={classes.root} spacing={2}>
                <Grid item xs={12}>
                    <Grid container justify="center" spacing={6}>
                        <ChatroomCard/>
                        <ChatroomCard/>
                        <ChatroomCard/>
                        <ChatroomCard/>
                        <ChatroomCard/>
                        <ChatroomCard/>
                        <ChatroomCard/>
                        <ChatroomCard/>
                        <ChatroomCard/>
                        <ChatroomCard/>
                    </Grid>
                </Grid>
            </Grid>
        </div>
    )
    
}
ChatroomCardGrid.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(ChatroomCardGrid)