import React, {useState} from 'react';
import './css/App.css';
import DiagramTable from "./DiagramTable";
import AddForm from "./AddForm";
import {AppBar, createTheme, Tab, ThemeProvider, Toolbar, Typography} from "@mui/material";
import TabContext from '@mui/lab/TabContext';
import TabList from '@mui/lab/TabList';
import TabPanel from '@mui/lab/TabPanel';
import IconButton from "@mui/material/IconButton";
import MenuButton from "@mui/icons-material/Menu";

function App() {
    const [value, setValue] = useState("Overview")

    const theme = createTheme({
        palette: {
            primary: {
                main: '#fc5d0d',
            },
            secondary: {
                main: '#336571',
            },
        },
    })

  return (
    <ThemeProvider theme={theme}>
        <header>
            <AppBar position="static">
                <Toolbar>
                    <IconButton>
                        <MenuButton fontSize="large"/>
                    </IconButton>
                    <Typography variant="h2">BPMN-Library</Typography>
                </Toolbar>
            </AppBar>
        </header>
            <TabContext value={value}>
                <AppBar position="static">
                    <Toolbar >
                        <TabList
                            textColor="secondary"
                            indicatorColor="secondary"
                            onChange={ (event, newValue: string) => setValue(newValue)}
                            aria-label="tabs"
                        >
                            <Tab value="Overview" label="Process Overview" color="primary"/>
                            <Tab value="Add" label="Add Process" />
                            <Tab value="Details" label="Process Details" />
                        </TabList>
                    </Toolbar>
                </AppBar>
                <TabPanel value="Overview">
                    <DiagramTable/>
                </TabPanel>
                <TabPanel value="Add">
                    <AddForm/>
                </TabPanel>
                <TabPanel value="Details">Details</TabPanel>
            </TabContext>
    </ThemeProvider>
  );
}

export default App;
