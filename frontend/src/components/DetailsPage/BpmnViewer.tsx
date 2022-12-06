import React from 'react';
import ReactBpmn from 'react-bpmn';
import {Card, CardContent, Typography} from "@mui/material";
import {BpmnDiagramModel} from "../../model/BpmnDiagramModel";

type BpmnViewerProps = {
    diagram: BpmnDiagramModel
}

function BpmnViewer(props: BpmnViewerProps) {

    function onLoading() {
        return <Typography variant="body2">Loading...</Typography>
    }

    function onError() {
        return <Typography variant="body2">No Diagram can be displayed</Typography>
    }

    return (
        <Card>
            {props.diagram.customDiagram ?
                <CardContent>
                    <Typography variant="h5" color="secondary">Viewer</Typography>
                    <Typography variant="body2">No XML-Data available</Typography>
                </CardContent>
                :
                <CardContent>
                    <Typography variant="h5" color="secondary">Viewer</Typography>
                    <ReactBpmn
                        url={"/api/camundaprocesses/" + props.diagram.id + "/xml"}
                        onLoading={onLoading}
                        onError={onError}
                    />
                </CardContent>
            }
        </Card>
    );
}

export default BpmnViewer;