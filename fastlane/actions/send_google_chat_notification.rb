module Fastlane
  module Actions
    class SendGoogleChatNotificationAction < Action
      def self.run(params)
        version_name = params[:version_name]
        webhook_url  = params[:webhook_url]
        api_token    = params[:gh_token]

        UI.header("Generating Jira release notes for v#{version_name}")

        # Get release body
        release_info = other_action.github_api(
          api_token: api_token,
          http_method: 'GET',
          path: "repos/Jeff-Soares/workflow/releases/tags/#{version_name}"
        )
        release_body = release_info[:json]["body"]

        # Extract PR numbers
        pr_numbers = release_body.scan(/(?:pull|issues)\/(\d+)/).flatten.uniq
        UI.message "Pull Requests found: #{pr_numbers.join(', ')}"

        body_widgets = []

        pr_numbers.each do |pr|
          UI.message("Searching for Jira links in PR ##{pr}...")

          # Get PR details
          pr_info = other_action.github_api(
            api_token: api_token,
            http_method: 'GET',
            path: "repos/Jeff-Soares/workflow/pulls/#{pr}"
          )
          pr_data = pr_info[:json]
          pr_body = pr_data["body"] || ""
          pr_title = pr_data["title"] || ""

          # Links of type: [description](https://grupokabum.atlassian.net/...)
          links = pr_body.scan(/\[(.*?)\]\((https:\/\/grupokabum\.atlassian\.net\/.*?)\)/)

          if links.empty?
            UI.message("No Jira links found in PR ##{pr}, using PR title instead - #{pr_title}")
            body_widgets << text_widget("• #{pr_title}")
          else
            links.each do |desc, url|
              UI.message("Found: #{desc} -> #{url}")
              body_widgets << text_widget("• <a href='#{url}'>#{desc}</a>")
            end
          end
        end

        sections = []
        sections << section(header: "O que mudou:", widgets: body_widgets) if body_widgets.any?
        sections << section(
          header: "Github:",
          widgets: [ text_widget("🏷️ <a href='https://github.com/kabum/mobile-android/releases/tag/v#{version_name}'>v#{version_name}</a>") ]
        )

        payload = card(
          header: header(
            title: "Release v#{version_name} 🚀",
            subtitle: "Enviado ao Google Play",
            image_url: "https://developers.google.com/static/site-assets/images/products/android-logo.png"
          ),
          sections: sections
        )

        json_payload = JSON.pretty_generate(payload)
        UI.message("Payload:\n#{json_payload}")

        # Send to Google Chat
        curl_cmd = [
          "curl", "-X", "POST", "#{webhook_url}",
          "-H", "Content-Type: application/json",
          "--data", json_payload
        ]

        system(*curl_cmd) || UI.error("Failed to send message to Google Chat")
      end

      def self.available_options
        [
          FastlaneCore::ConfigItem.new(key: :version_name,
            description: "Release version name",
            optional: false,
            type: String),
          FastlaneCore::ConfigItem.new(key: :webhook_url,
            description: "Google Chat webhook URL",
            optional: false,
            type: String),
          FastlaneCore::ConfigItem.new(key: :gh_token,
            description: "Github TOKEN to access release info",
            optional: false,
            type: String)
        ]
      end

      def self.description
        "Generate Jira release notes and send to Google Chat Workspace"
      end

      def self.is_supported?(platform)
        true
      end

      class << self
        private
        def header(title:, subtitle:, image_url:)
          { title: title, subtitle: subtitle, imageUrl: image_url }
        end

        def text_widget(text)
          { textParagraph: { text: text } }
        end

        def section(header:, widgets:, collapsible: false)
          { header: header, collapsible: collapsible, widgets: widgets }
        end

        def card(header:, sections:)
          { cardsV2: [{ card: { header: header, sections: sections } }] }
        end
      end

    end
  end
end
