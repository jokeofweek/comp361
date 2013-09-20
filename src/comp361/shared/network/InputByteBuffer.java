package comp361.shared.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import comp361.shared.Constants;

/**
 * This class takes bytes created by an {@link OutputByteBuffer} and allows for
 * extraction of values.
 */
public class InputByteBuffer {

	private byte[] bytes;
	private int pos;

	public InputByteBuffer(byte[] bytes) {
		this.bytes = bytes;
		this.reset();
	}

	/**
	 * This reads a file to completion and then wraps it in an input byte
	 * buffer.
	 * 
	 * @throws IOException
	 * 
	 * @pram file the file to read.
	 */
	public InputByteBuffer(File file) throws IOException {
		this.bytes = new byte[(int) file.length()];
		int offset = 0;
		int read = 0;

		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			while (offset < this.bytes.length) {
				read = in.read(this.bytes, offset, this.bytes.length - offset);
				if (read == -1) {
					throw new IOException(
							"Could could not read any bytes. End of stream.");
				}
				offset += read;
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}

		this.pos = 0;

	}

	/**
	 * This resets the internal pointer, allowing you to go through the byte
	 * buffer again.
	 */
	public void reset() {
		this.pos = 0;
	}

	public boolean atEnd() {
		return this.pos == this.bytes.length;
	}

	public void reserveCapacity(int capacity) {
		if (this.bytes.length < pos + capacity) {
			throw new ByteBufferOutOfBoundsException(this.bytes.length,
					this.pos + capacity);
		}
	}

	public byte getByte() {
		reserveCapacity(1);
		pos++;
		return bytes[this.pos - 1];
	}

	public short getShort() {
		reserveCapacity(2);
		short v = bytes[this.pos];
		v <<= 8;
		v |= (bytes[this.pos + 1] & 0xff);
		pos += 2;
		return v;
	}

	public int getUnsignedShort() {
		reserveCapacity(2);
		int v = bytes[this.pos];
		v <<= 8;
		v |= (bytes[this.pos + 1] & 0xff);
		v &= 0x0ffff;
		pos += 2;
		return v;
	}

	public int getInt() {
		reserveCapacity(4);
		int v = bytes[this.pos];
		v <<= 8;
		v |= (bytes[this.pos + 1] & 0xff);
		v <<= 8;
		v |= (bytes[this.pos + 2] & 0xff);
		v <<= 8;
		v |= (bytes[this.pos + 3] & 0xff);
		pos += 4;
		return v;
	}

	public long getLong() {
		reserveCapacity(8);
		long v = bytes[this.pos];
		v <<= 8;
		v |= (bytes[this.pos + 1] & 0xff);
		v <<= 8;
		v |= (bytes[this.pos + 2] & 0xff);
		v <<= 8;
		v |= (bytes[this.pos + 3] & 0xff);
		v <<= 8;
		v |= (bytes[this.pos + 4] & 0xff);
		v <<= 8;
		v |= (bytes[this.pos + 5] & 0xff);
		v <<= 8;
		v |= (bytes[this.pos + 6] & 0xff);
		v <<= 8;
		v |= (bytes[this.pos + 7] & 0xff);
		pos += 8;
		return v;
	}

	public boolean getBoolean() {
		reserveCapacity(1);
		pos++;
		return bytes[pos - 1] == 1;
	}

	public String getString() {
		int length = getInt();
		reserveCapacity(length);
		String v = new String(Arrays.copyOfRange(bytes, this.pos, this.pos
				+ length), Constants.CHARSET);
		pos += length;
		return v;
	}

	public char[] getCharArray() {
		int length = getInt();
		reserveCapacity(length);
		char[] chars = new char[length];
		for (int i = 0; i < length; i++) {
			chars[i] = (char) (((bytes[pos] & 0xff) << 8) | (bytes[pos + 1] & 0xff));
			pos += 2;
		}

		return chars;
	}

	/**
	 * This class is thrown when too many bytes are read.
	 */
	public class ByteBufferOutOfBoundsException extends RuntimeException {

		private static final long serialVersionUID = -1795424603877796335L;

		public ByteBufferOutOfBoundsException(int length, int index) {
			super(
					String.format(
							"Attempted to read out of byte buffer bounds.\nCurrent length: %d\nAttempted read at: %d.",
							length, index));
		}
	}
}