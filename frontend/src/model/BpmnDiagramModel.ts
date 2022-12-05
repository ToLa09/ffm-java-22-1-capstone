import {CommentModel} from "./CommentModel";

export type BpmnDiagramModel = {
    id: string
    name: string
    businessKey: string
    filename: string
    version: number
    comments: CommentModel[]
    customDiagram: boolean
}
