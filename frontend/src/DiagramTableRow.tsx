import React, {Dispatch, SetStateAction, useState} from 'react';
import {BpmnDiagramModel} from "./model/BpmnDiagramModel";
import './css/DiagramLine.css';
import TableCell from "@mui/material/TableCell";
import TableRow from "@mui/material/TableRow";
import {Button} from "@mui/material";

export type Props = {
    diagram: BpmnDiagramModel
    fetchDiagrams: () => void
    setValue: Dispatch<SetStateAction<string>>
    setDetailedDiagram: Dispatch<SetStateAction<BpmnDiagramModel>>
}

export default function DiagramTableRow(props: Props) {

    const[editMode, setEditMode] = useState<boolean>(false)
    const[unfold, setUnfold] = useState<boolean>(false)

    const handleDetails = () => {
        props.setValue("Details")
        props.setDetailedDiagram(props.diagram)
    }

    return (
        <>
            <TableRow key={props.diagram.id} >
                <TableCell component="td" scope="row">{props.diagram.name}</TableCell>
                <TableCell align="left">{props.diagram.filename}</TableCell>
                <TableCell align="center">{props.diagram.version}</TableCell>
                <TableCell align="left">{props.diagram.commentText}</TableCell>
                <TableCell align="center"><Button onClick={handleDetails} color="secondary">Details</Button></TableCell>
            </TableRow>
            {/*<TableRow key={props.diagram.id}>*/}
                {/*{ editMode ?*/}
                {/*    <>*/}
                {/*        <TableCell component="td" scope="row"><input placeholder={"name"} value={name} onChange={(event) => setName(event.target.value)}/></TableCell>*/}
                {/*        <TableCell align="right"><input placeholder={"businessKey"} value={businessKey} onChange={(event) => setBusinessKey(event.target.value)}/></TableCell>*/}
                {/*        <TableCell align="right"><input placeholder={"xmlFile"} value={filename} onChange={(event) => setFilename(event.target.value)}/></TableCell>*/}
                {/*        <TableCell align="right"><input placeholder={"comment"} value={comment} onChange={(event) => setComment(event.target.value)}/></TableCell>*/}
                {/*    </>*/}
                {/*    :*/}
                {/*    <>*/}
                {/*        <TableCell>*/}
                {/*            <IconButton*/}
                {/*                aria-label="expand row"*/}
                {/*                size="small"*/}
                {/*                onClick={() => setUnfold(!unfold)}*/}
                {/*            >*/}
                {/*                {unfold ? <KeyboardArrowUpIcon/> : <KeyboardArrowDownIcon/>}*/}
                {/*            </IconButton>*/}
                {/*        </TableCell>*/}
                {/*        <TableCell component="td" scope="row">{props.diagram.name}</TableCell>*/}
                {/*        <TableCell align="right">{props.diagram.filename}</TableCell>*/}
                {/*        <TableCell align="right">{props.diagram.version}</TableCell>*/}
                {/*        <TableCell align="right">{props.diagram.commentText}</TableCell>*/}
                {/*    </>*/}
                {/*}*/}
                {/*<TableCell component="th" scope="row">*/}
                {/*    {editMode ?*/}
                {/*        <IconButton onClick={() => {*/}
                {/*            setEditMode(false)*/}
                {/*            handleUpdate()*/}
                {/*        }} >*/}
                {/*            <CheckIcon color="secondary"/>*/}
                {/*        </IconButton>*/}
                {/*        :*/}
                {/*        <IconButton onClick={() => setEditMode(true)}><EditIcon color="primary" /></IconButton>*/}
                {/*    }*/}
                {/*    <IconButton onClick={() => handleDuplicate(props.diagram)}><ContentCopyIcon color="secondary" /></IconButton>*/}
                {/*    <IconButton onClick={() => handleDelete(props.diagram.id)}><DeleteForeverIcon color="error" /></IconButton>*/}
                {/*</TableCell>*/}
            {/*</TableRow>*/}
        </>
    );
}