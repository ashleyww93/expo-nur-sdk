package ai.cloudshelf.exponursdk;

import com.nordicid.nurapi.NurPacket;

public class RingBuffer {
    public static int DefaultBufferSize = 16384;
    private int mReadPos;
    private int mWritePos;
    private int mCount;
    private byte[] mData;
    private int mDataSize;

    public RingBuffer() {
        this(-1);
    }

    public RingBuffer(int size) {
        this.mReadPos = 0;
        this.mWritePos = 0;
        this.mCount = 0;
        if (size == -1) {
            size = DefaultBufferSize;
        }

        this.mDataSize = size;
        this.mData = new byte[size];
    }

    public void Reset() {
        this.mCount = 0;
        this.mReadPos = 0;
        this.mWritePos = 0;
    }

    public byte[] getBuffer() {
        return this.mData;
    }

    public int getReadPos() {
        return this.mReadPos;
    }

    public int getWritePos() {
        return this.mWritePos;
    }

    public int getCount() {
        return this.mCount;
    }

    public int getDataSize() {
        return this.mDataSize;
    }

    public int isAvailable() {
        return this.mDataSize - this.mCount;
    }

    public boolean isEmpty() {
        return this.mReadPos == this.mWritePos && this.mCount == 0;
    }

    public boolean isFull() {
        return this.mReadPos == this.mWritePos && this.mCount > 0;
    }

    public void Write(byte[] buffer) {
        this.Write(buffer, 0, buffer.length);
    }

    public void Write(byte[] buffer, int bufferLen) {
        this.Write(buffer, 0, bufferLen);
    }

    public void Write(byte[] buffer, int bufferOffset, int bufferLen) {
        synchronized(this) {
            if (this.mCount + bufferLen > this.mDataSize) {
                throw new IndexOutOfBoundsException("Buffer Overflow! count, buf (total), size = " + this.mCount + ", " + bufferLen + " (" + (this.mCount + bufferLen) + "), " + this.mDataSize + ".");
            } else {
                if (this.mWritePos + bufferLen > this.mDataSize) {
                    int len = this.mDataSize - this.mWritePos;
                    System.arraycopy(buffer, bufferOffset, this.mData, this.mWritePos, len);
                    this.mCount += len;
                    this.mWritePos = 0;
                    bufferOffset += len;
                    bufferLen -= len;
                }

                System.arraycopy(buffer, bufferOffset, this.mData, this.mWritePos, bufferLen);
                this.mCount += bufferLen;
                this.mWritePos = (this.mWritePos + bufferLen) % this.mDataSize;
            }
        }
    }

    public void Read(byte[] buffer) {
        this.Read(buffer, 0, buffer.length);
    }

    public void Read(byte[] buffer, int bufferLen) {
        this.Read(buffer, 0, bufferLen);
    }

    public void Read(byte[] buffer, int bufferOffset, int bufferLen) {
        synchronized(this) {
            if (this.mCount + bufferLen > this.mDataSize) {
                throw new IndexOutOfBoundsException("Buffer Underflow!");
            } else {
                if (this.mReadPos + bufferLen > this.mDataSize) {
                    int len = this.mDataSize - this.mReadPos;
                    System.arraycopy(this.mData, this.mReadPos, buffer, bufferOffset, len);
                    this.mReadPos = 0;
                    bufferOffset += len;
                    bufferLen -= len;
                    this.mCount -= len;
                }

                System.arraycopy(this.mData, this.mReadPos, buffer, bufferOffset, bufferLen);
                this.mCount -= bufferLen;
                this.mReadPos = (this.mReadPos + bufferLen) % this.mDataSize;
            }
        }
    }

    public void Peek(byte[] buffer) {
        this.Peek(buffer, 0, buffer.length, 0);
    }

    public void Peek(byte[] buffer, int bufferLen) {
        this.Peek(buffer, 0, bufferLen, 0);
    }

    public void Peek(byte[] buffer, int bufferOffset, int bufferLen) {
        this.Peek(buffer, bufferOffset, bufferLen, 0);
    }

    public void Peek(byte[] buffer, int bufferOffset, int bufferLen, int srcOffset) {
        synchronized(this) {
            if (bufferLen + srcOffset > this.mCount) {
                throw new IndexOutOfBoundsException("Buffer Underflow!");
            } else {
                int rPos = (this.mReadPos + srcOffset) % this.mDataSize;
                if (rPos + bufferLen > this.mDataSize) {
                    int len = this.mDataSize - rPos;
                    System.arraycopy(this.mData, rPos, buffer, bufferOffset, len);
                    rPos = 0;
                    bufferOffset += len;
                    bufferLen -= len;
                }

                System.arraycopy(this.mData, rPos, buffer, bufferOffset, bufferLen);
            }
        }
    }

    public void DiscardBytes(int val) {
        synchronized(this) {
            if (this.mCount < val) {
                val = this.mCount;
            }

            if (val > 0) {
                this.mCount -= val;
                this.mReadPos = (this.mReadPos + val) % this.mDataSize;
            }

        }
    }

    public int PeekByte(int offset) {
        synchronized(this) {
            if (offset + 1 > this.mCount) {
                throw new IndexOutOfBoundsException("Buffer Underflow!");
            } else {
                int rPos = (this.mReadPos + offset) % this.mDataSize;
                return NurPacket.BytesToByte(this.mData, rPos);
            }
        }
    }

    public int PeekWord(int offset) {
        synchronized(this) {
            return (this.PeekByte(offset + 0) & 255 | (this.PeekByte(offset + 1) & 255) << 8) & '\uffff';
        }
    }

    public int PeekDword(byte[] buf, int offset) {
        synchronized(this) {
            return (this.PeekByte(offset + 0) & 255 | (this.PeekByte(offset + 1) & 255) << 8 | (this.PeekByte(offset + 2) & 255) << 16 | (this.PeekByte(offset + 3) & 255) << 24) & -1;
        }
    }
}
