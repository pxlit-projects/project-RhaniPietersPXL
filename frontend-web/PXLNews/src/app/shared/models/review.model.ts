import {Post} from "./post.model";

export class Review {
    id?: number;
    rejectedMessage: string;
    post: Post;

    constructor(rejectedMessage: string, post: Post) {
        this.rejectedMessage = rejectedMessage;
        this.post = post;
    }
}


