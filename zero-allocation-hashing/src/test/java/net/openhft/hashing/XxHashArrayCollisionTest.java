package net.openhft.hashing;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class XxHashArrayCollisionTest {

    @Test
    public void xxHashArrayCollisionTest() {

        ByteBuffer sequence = ByteBuffer.allocate(128);
        sequence.order(ByteOrder.LITTLE_ENDIAN);

        long[] hasharray1= new long[3];
        hasharray1[0] = LongHashFunction.xx().hashChars("XX HASH TEST");

        hasharray1[1] = LongHashFunction.xx().hashChars("OBJECT-ORIENTED LANGUAGES AND ENVIRONMENT");

        sequence.putLong(0, 1 + 0xBA79078168D4BAFL * 101);
        sequence.putLong(32, 11 + 0x9C90005B80000000L * 101);
        hasharray1[2] = LongHashFunction.xx().hashBytes(sequence);

        // Insertion of hash values in array 2

        long[] hasharray2= new long[3];

        hasharray2[0] = LongHashFunction.xx().hashChars("XX HASH TEST");

        hasharray2[1] = LongHashFunction.xx().hashChars("OBJECT-ORIENTED LANGUAGES AND ENVIRONMENT");

        sequence.putLong(0, 1 + 0xBA79078168D4BAFL * 201);
        sequence.putLong(32, 11 + 0x9C90005B80000000L * 201);
        hasharray2[2] = LongHashFunction.xx().hashBytes(sequence);

        assertArrayEquals(hasharray1,hasharray2);
    }
}
