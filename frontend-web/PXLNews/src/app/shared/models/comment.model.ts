export class Comment {
    id?: number;
    title: string;
    content: string;
    author: string;
    postId: number

    constructor(title: string, content: string, author: string, postId: number) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.postId = postId;
    }
}


