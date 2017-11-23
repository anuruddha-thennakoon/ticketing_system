import { BaseEntity } from './../../shared';

export class TransportManager implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public nic?: string,
        public timeTables?: BaseEntity[],
    ) {
    }
}
