package com.citius.filesPrograms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.TimeUnit;

public class CreateLargeFile {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {

			File fileObject = new File("D:\\testFolder\\textfile.txt");

			CreateLargeFile crf = new CreateLargeFile();
			crf.createFileWithByteBuffer(fileObject);

			// crf.readFileWithFileSizeBuffer(fileObject);
			crf.readFileWithoutNIO(fileObject);
			crf.readFileWithFixedSizeBuffer(fileObject);
			crf.readFileWithMappedByteBuffer(fileObject);
			
			System.out.println("File Read");

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	private void createFileWithByteBuffer(File fileObject) {
		try {
			long startTime = System.nanoTime();
			byte[] buffer = "Help I am trapped in a fortune cookie factory\n"
					.getBytes();
			int number_of_lines = 40000000; // Forty Million Lines
			FileChannel rwChannel = null;
			rwChannel = new RandomAccessFile(fileObject, "rw").getChannel();
			ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0,
					buffer.length * number_of_lines);
			for (int i = 0; i < number_of_lines; i++) {
				wrBuf.put(buffer);
			}
			rwChannel.close();

			long endTime = System.nanoTime();
			long estimatedTime = endTime - startTime;

			double bytes = fileObject.length();
			double kilobytes = (bytes / 1024);
			double megabytes = (kilobytes / 1024);
			double gigabytes = (megabytes / 1024);
			gigabytes = Math.ceil(gigabytes);
			System.out.println("createFileWithByteBuffer took : "
					+ TimeUnit.SECONDS.convert(estimatedTime,
							TimeUnit.NANOSECONDS)
					+ "Seconds to create a file of : " + gigabytes + " GB");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void readFileWithoutNIO(File fileObject) {

		BufferedReader br = null;
		String sCurrentLine = null;
		try {
			int count = 0;
			long startTime = System.nanoTime();
			br = new BufferedReader(new FileReader(fileObject));
			while ((sCurrentLine = br.readLine()) != null) {
				// System.out.println(sCurrentLine);
				++count;
			}

			long endTime = System.nanoTime();
			long estimatedTime = endTime - startTime;

			double bytes = fileObject.length();
			double kilobytes = (bytes / 1024);
			double megabytes = (kilobytes / 1024);
			double gigabytes = (megabytes / 1024);
			gigabytes = Math.ceil(gigabytes);
			System.out.println(" readFileWithoutNIO  took  : "
					+ TimeUnit.SECONDS.convert(estimatedTime,
							TimeUnit.NANOSECONDS) + ""
					+ "seconds For a file of : " + gigabytes + " GB");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	private void readFileWithFileSizeBuffer(File fileObject) {

		try {
			long startTime = System.nanoTime();
			RandomAccessFile aFile = new RandomAccessFile(fileObject, "r");
			FileChannel inChannel = aFile.getChannel();
			long fileSize = inChannel.size();
			ByteBuffer bufferRead = ByteBuffer.allocate((int) fileSize);
			inChannel.read(bufferRead);
			// buffer.rewind();
			bufferRead.flip();
			for (int i = 0; i < fileSize; i++) {
				// System.out.print((char) bufferRead.get());
			}
			inChannel.close();
			aFile.close();
			long endTime = System.nanoTime();
			long estimatedTime = endTime - startTime;

			double bytes = fileObject.length();
			double kilobytes = (bytes / 1024);
			double megabytes = (kilobytes / 1024);
			double gigabytes = (megabytes / 1024);
			gigabytes = Math.ceil(gigabytes);
			System.out.println(" readFileWithFileSizeBuffer  took  : "
					+ TimeUnit.SECONDS.convert(estimatedTime,
							TimeUnit.NANOSECONDS) + ""
					+ "seconds For a file of : " + gigabytes + " GB");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void readFileWithFixedSizeBuffer(File fileObject) {

		try {
			long startTime = System.nanoTime();
			RandomAccessFile aFile = new RandomAccessFile(fileObject, "r");
			FileChannel inChannel = aFile.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			while (inChannel.read(buffer) > 0) {
				buffer.flip();
				for (int i = 0; i < buffer.limit(); i++) {
					// System.out.print((char) buffer.get());
					buffer.get();
				}
				buffer.clear(); // do something with the data and clear/compact
								// it.
			}
			inChannel.close();
			aFile.close();
			long endTime = System.nanoTime();
			long estimatedTime = endTime - startTime;

			double bytes = fileObject.length();
			double kilobytes = (bytes / 1024);
			double megabytes = (kilobytes / 1024);
			double gigabytes = (megabytes / 1024);
			gigabytes = Math.ceil(gigabytes);
			System.out.println("readFileWithFixedSizeBuffer took  : "
					+ TimeUnit.SECONDS.convert(estimatedTime,
							TimeUnit.NANOSECONDS) + ""
					+ "seconds For a file of : " + gigabytes + " GB");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void readFileWithMappedByteBuffer(File fileObject) {

		try {
			long startTime = System.nanoTime();
			RandomAccessFile aFile = new RandomAccessFile(fileObject, "r");
			FileChannel inChannel = aFile.getChannel();
			MappedByteBuffer buffer = inChannel.map(
					FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
			buffer.load();
			for (int i = 0; i < buffer.limit(); i++) {
				// System.out.print((char) buffer.get());
				buffer.get();
			}
			buffer.clear(); // do something with the data and clear/compact it.
			inChannel.close();
			aFile.close();
			long endTime = System.nanoTime();
			long estimatedTime = endTime - startTime;
			// System.out.println("duration in MilliSeconds : "+TimeUnit.MILLISECONDS.convert(estimatedTime,
			// TimeUnit.NANOSECONDS));

			double bytes = fileObject.length();
			double kilobytes = (bytes / 1024);
			double megabytes = (kilobytes / 1024);
			double gigabytes = (megabytes / 1024);
			gigabytes = Math.ceil(gigabytes);
			System.out.println("readFileWithMappedByteBuffer took  : "
					+ TimeUnit.SECONDS.convert(estimatedTime,
							TimeUnit.NANOSECONDS) + ""
					+ "seconds For a file of : " + gigabytes + " GB");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
