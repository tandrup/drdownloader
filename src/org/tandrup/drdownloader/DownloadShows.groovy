package org.tandrup.drdownloader

import groovy.json.JsonSlurper

def cli = new CliBuilder(usage:'DownloadShows');
cli.with {
	i "Postfix an index"
}

def options = cli.parse(args);

def slug = options.arguments()[0];

def dir = new File(slug);
dir.mkdir();

println "Downloading image"
def out = new BufferedOutputStream(new FileOutputStream(new File(dir, "${slug}.jpg")))
out << new URL("http://www.dr.dk/NU/api/programseries/$slug/images/400x300.jpg").openStream()
out.close()

println "Downloading list of $slug episodes"

def json = new JsonSlurper().parseText(new URL("http://www.dr.dk/NU/api/programseries/${slug}/videos").text);

def index = 1;

json.sort{it.broadcastTime}.each {
	def mediaUrl = new URL(it.videoManifestUrl).text.replace("rtmp://vod.dr.dk/cms/mp4:", "http://vodfiles.dr.dk/");
	mediaUrl = mediaUrl.substring(0, mediaUrl.indexOf("?ID="));
	def filename = it.title.replace(":", "-");
	if (options.i) {
		filename += " $index";
		index++;
	}
	
	println "MediaURL $mediaUrl";

//	out = new BufferedOutputStream(new FileOutputStream(new File(dir, "${filename}.jpg")));
//	out << new URL("http://www.dr.dk/NU/api/videos/${it.id}/images/400x300.jpg").openStream();
//	out.close();
//	
//	def file = new File(dir, filename + ".mp4")
//	println "Downloading ${it.title} to $file";
//	if (!file.exists()) {
//		out = new BufferedOutputStream(new FileOutputStream(file))
//		out << new URL(mediaUrl).openStream()
//		out.close()
//		println "Done"
//		sleep 1000
//	}
}

println "Bye. No more to do"