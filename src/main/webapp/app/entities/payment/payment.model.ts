import { BaseEntity } from './../../shared';

export class Payment implements BaseEntity {
    constructor(
        public id?: number,
        public amount?: number,
        public paymentDate?: string,
        public journey?: BaseEntity,
        public smartCard?: BaseEntity,
    ) {
    }
}
