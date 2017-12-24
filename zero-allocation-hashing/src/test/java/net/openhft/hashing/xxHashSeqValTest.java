package net.openhft.hashing;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Random;

public class xxHashSeqValTest {


        @Test
        public void xxHashSeqValTest() {
            ByteBuffer sequence = ByteBuffer.allocate(128);
            sequence.order(ByteOrder.LITTLE_ENDIAN);
            sequence.putLong(0, 1 + 0xBA79078168D4BAFL * 40);
            sequence.putLong(32, 11 + 0x9C90005B80000000L * 40);
            assertNotNull( LongHashFunction.xx().hashBytes(sequence));
            String hashstr = "";
            assertNotNull( LongHashFunction.xx().hashChars(hashstr));
        }


}
